package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.Manifold
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.state.PlayerState
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationData
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.AttackMeta
import com.libgdx.treasurehunter.ecs.components.Collectable
import com.libgdx.treasurehunter.ecs.components.Damage
import com.libgdx.treasurehunter.ecs.components.DamageTaken
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.ItemType
import com.libgdx.treasurehunter.ecs.components.Life
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Particle
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.event.GameEventListener
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.tiled.sprite
import com.libgdx.treasurehunter.utils.GameObject
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2
import ktx.math.plus

class PhysicSystem (
    private val physicWorld : PhysicWorld = inject(),
    private val assetHelper: AssetHelper = inject()
): IteratingSystem(
    family = family{all(Physic, Graphic)},
    interval = Fixed(1/300f)
) , ContactListener, GameEventListener{

    init {
        physicWorld.setContactListener(this)
    }

    private var removeEntityList = mutableSetOf<Entity>()

    override fun onUpdate() {
        if (physicWorld.autoClearForces){
            physicWorld.autoClearForces = false
        }
        super.onUpdate()
        physicWorld.clearForces()
    }

    override fun onTick() {
        super.onTick()
        physicWorld.step(deltaTime,6,2)
    }

    override fun onTickEntity(entity: Entity) {
        val physicComp = entity[Physic]
        val (body,previousPosition) = physicComp

        previousPosition.set(body.position)
        entity.getOrNull(Move)?.let {moveComp ->
            body.setLinearVelocity(moveComp.currentSpeed , body.linearVelocity.y)
        }
        if (removeEntityList.isNotEmpty()){
            removeEntityList.forEach { entity ->
                entity.remove()
            }
            removeEntityList.clear()
        }
    }

    override fun onAlphaEntity(entity: Entity, alpha: Float) {
        val graphic = entity[Graphic]
        val (sprite) = graphic
        val effectOffset = graphic.effectOffset
        val physic = entity[Physic]
        val (body,previousPosition) = physic
        val (prevX,prevY) = previousPosition
        val (bodyX,bodyY) = body.position

        sprite.setPosition(
            MathUtils.lerp(prevX,bodyX,alpha) + effectOffset.x,
            MathUtils.lerp(prevY,bodyY,alpha) + effectOffset.y
        )
        if (entity has AttackMeta){
            sprite.setPosition(bodyX + effectOffset.x,bodyY + effectOffset.y)
        }

    }

    private val Fixture.entity : Entity?
        get() {
            val userData = this.body.userData
            return userData as? Entity
        }

    companion object{
        val Fixture.isGround : Boolean
            get() = this.userData == "ground"
        val Fixture.isPlatform : Boolean
            get() = this.userData == "platform"
        val Fixture.isFlag : Boolean
            get() = this.userData == "flag"
        val Fixture.isShipHelm : Boolean
            get() = this.userData == "shipHelm"
        val Fixture.isChest : Boolean
            get() = this.userData == "chest"

    }
    private val Fixture.isPlayerFoot : Boolean
        get() = this.userData == "footFixture"

    private val Fixture.isHitbox : Boolean
        get() = this.userData == "hitbox"

    private val Entity.isPlayer : Boolean
        get() = this.has(EntityTag.PLAYER)

    private val Fixture.isRangeAttackFixture : Boolean
        get() = this.userData == "rangeAttackFixture"

    private val Fixture.isSensorFixture : Boolean
        get() = this.userData == "sensorFixture"

    //----- HANDLE COLLISIONS -----
    private fun handleDamageBeginContact(damageSource: Entity, damageTarget: Entity) {
        val (damageAmount,sourceEntity) = damageSource[Damage]
        if (sourceEntity == damageTarget) return
        damageTarget.configure {
            val damageTakenComp = it.getOrAdd(DamageTaken){ DamageTaken(0) }
            damageTakenComp.damageAmount = (damageTakenComp.damageAmount + damageAmount).coerceAtMost(damageAmount)
        }
    }

    private fun handleDamageEndContact(damageSource: Entity, damageTarget: Entity) {
        damageTarget.getOrNull(DamageTaken)?.let {
            val (damageAmount) = damageSource[Damage]
            it.damageAmount -= damageAmount
            if (it.damageAmount <= 0){
                damageTarget.configure { it -= DamageTaken }
            }
        }
    }

    private fun handleCollectableBeginContact(collectableEntity: Entity, playerEntity: Entity) {
        GameEventDispatcher.fireEvent(GameEvent.CollectableItemEvent(collectableEntity,playerEntity))
    }

    private fun handleSwordAndWallCollision(activeAttackEntity: Entity) {
        val attackMeta = activeAttackEntity[AttackMeta]
        attackMeta.collidedWithWall = true
    }

    // ----- /HANDLE COLLISIONS -----

    // ----- CHECK CONDITIONS -----
    private fun isDamageCollision(entityA: Entity,entityB: Entity,fixtureB:Fixture) : Boolean{
        return  entityA has Damage  && entityB has Life && fixtureB.isSensor && fixtureB.isHitbox
    }

    private fun isCollectableCollision(entityA: Entity,entityB: Entity,fixtureB:Fixture) : Boolean{
        return entityA has EntityTag.COLLECTABLE && entityB has EntityTag.PLAYER && fixtureB.isSensor && !fixtureB.isSensorFixture
    }

    private fun isSwordAndWallCollision(entityA: Entity?,fixtureA : Fixture,fixtureB:Fixture) : Boolean{
        return entityA != null && entityA has AttackMeta && fixtureA.isRangeAttackFixture && !fixtureB.isSensor
    }


    // -----/ CHECK CONDITIONS -----

    override fun beginContact(contact: Contact) {
        val fixtureA = contact.fixtureA
        val fixtureB = contact.fixtureB
        val entityA = fixtureA.entity
        val entityB = fixtureB.entity

        if (entityA == null || entityB == null) {
            when{
                isSwordAndWallCollision(entityA,fixtureA,fixtureB) -> handleSwordAndWallCollision(entityA!!)
                isSwordAndWallCollision(entityB,fixtureB,fixtureA) -> handleSwordAndWallCollision(entityB!!)
                else -> Unit
            }
            return
        }
        when {
            isCollectableCollision(entityA,entityB,fixtureB) -> handleCollectableBeginContact(entityA,entityB)
            isCollectableCollision(entityB,entityA,fixtureA) -> handleCollectableBeginContact(entityB,entityA)
            isSwordAndWallCollision(entityA,fixtureA,fixtureB) -> handleSwordAndWallCollision(entityA)
            isSwordAndWallCollision(entityB,fixtureB,fixtureA) -> handleSwordAndWallCollision(entityB)
            isDamageCollision(entityA,entityB,fixtureB) -> handleDamageBeginContact(entityA,entityB)
            isDamageCollision(entityB,entityA,fixtureA) -> handleDamageBeginContact(entityB,entityA)
        }

    }

    override fun endContact(contact: Contact) {
        val fixtureA = contact.fixtureA
        val fixtureB = contact.fixtureB
        val entityA = fixtureA.entity
        val entityB = fixtureB.entity
        if (entityA == null || entityB == null){
            return
        }
        when {
            isDamageCollision(entityA,entityB,fixtureB) ->  handleDamageEndContact(entityA,entityB)
            isDamageCollision(entityB,entityA,fixtureA) -> handleDamageEndContact(entityB,entityA)
        }
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold) {
        val fixtureA = contact.fixtureA
        val fixtureB = contact.fixtureB
        val entityA = fixtureA.entity
        val entityB = fixtureB.entity
        if (fixtureA.isPlatform && entityB != null){
            contact.isEnabled = entityB[Physic].body.linearVelocity.y <= 0.0001f
        }
        if (fixtureB.isPlatform && entityA != null){
            contact.isEnabled = entityA[Physic].body.linearVelocity.y <= 0.0001f
        }
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {

    }


    override fun onEvent(event: GameEvent) {
        when(event){
            is GameEvent.RemoveEntityEvent -> {
                removeEntityList.add(event.entity)
            }
            is GameEvent.CollectableItemEvent ->{
                val collectableEntity = event.collectableEntity
                val playerEntity = event.playerEntity
                val (gameObject) = collectableEntity[Collectable]
                when(gameObject){
                    GameObject.SWORD ->{
                        if (playerEntity has Attack && playerEntity[Attack].attackItem is ItemType.Sword){
                            return
                        }
                        collectableEntity.configure {
                            it -= EntityTag.COLLECTABLE
                        }
                        playerEntity.configure {
                            it[State].stateMachine.changeState(PlayerState.SWORD_COLLECTED)
                        }
                    }
                    else -> Unit
                }
            }
            is GameEvent.ParticleEvent ->{
                val position = event.particlePosition
                world.entity{
                    it += Particle(event.particleType,event.owner)
                    it += Graphic(sprite(GameObject.DUST_PARTICLES, AnimationType.valueOf(event.particleType.name),position + vec2(0.2f,0.3f) , assetHelper ,0f))
                    it += Animation(GameObject.DUST_PARTICLES, animationData = AnimationData(
                        animationType = AnimationType.valueOf(event.particleType.name),
                        playMode = com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL
                    ))
                }
            }
            else -> Unit
        }
    }
}
