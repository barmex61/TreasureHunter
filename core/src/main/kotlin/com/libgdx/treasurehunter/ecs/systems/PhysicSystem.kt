package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
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
import com.libgdx.treasurehunter.ecs.components.AiComponent
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationData
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.AttackMeta
import com.libgdx.treasurehunter.ecs.components.Damage
import com.libgdx.treasurehunter.ecs.components.DamageTaken
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Jump
import com.libgdx.treasurehunter.ecs.components.Life
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Particle
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.components.Item
import com.libgdx.treasurehunter.ecs.components.Inventory
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventListener
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.tiled.sprite
import com.libgdx.treasurehunter.utils.GameObject
import ktx.math.component1
import ktx.math.component2
import ktx.math.div
import ktx.math.minus
import ktx.math.plus
import ktx.math.vec2

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
            val desiredSpeed = moveComp.currentSpeed
            val currentSpeed = body.linearVelocity.x
            val mass = body.mass
            val force = (desiredSpeed - currentSpeed) * mass * 10f
            body.applyForceToCenter(force, 0f, true)

        }
    }

    override fun onAlphaEntity(entity: Entity, alpha: Float) {
        val graphic = entity[Graphic]
        val (sprite) = graphic
        val effectOffset = graphic.offset
        val physic = entity[Physic]
        val (body,previousPosition) = physic
        val (prevX,prevY) = previousPosition
        val (bodyX,bodyY) = body.position
        sprite.apply {
            setPosition(
                MathUtils.lerp(prevX,bodyX,alpha) + effectOffset.x,
                MathUtils.lerp(prevY,bodyY,alpha) + effectOffset.y
            )
            rotation =  body.angle * MathUtils.radiansToDegrees
        }

    }

    companion object{
        val Fixture.entity : Entity?
            get() {
                val userData = this.body.userData
                return userData as? Entity
            }
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
        val Fixture.isChestLocked : Boolean
            get() = this.userData == "chest_locked"
        val Fixture.isBodyFixture : Boolean
            get() = this.userData == "bodyFixture"
        val Fixture.isRangeAttackFixture : Boolean
            get() = this.userData == "rangeAttackFixture"
        val Fixture.isMeleeAttackFixture : Boolean
            get() = this.userData == "meleeAttackFixture"
    }
    private val Fixture.isPlayerFoot : Boolean
        get() = this.userData == "footFixture"

    private val Fixture.isStaticBody : Boolean
        get() = this.body.type == BodyType.StaticBody

    private val Fixture.isDynamicBody : Boolean
        get() = this.body.type == BodyType.DynamicBody

    private val Fixture.isHitbox : Boolean
        get() = this.userData == "hitbox"

    private val Fixture.isBox : Boolean
        get() = this.userData == "box"

    private val Entity.isPlayer : Boolean
        get() = this.has(EntityTag.PLAYER)



    private val Fixture.isSensorFixture : Boolean
        get() = this.userData == "sensorFixture"

    //----- HANDLE COLLISIONS -----
    private fun handleDamageBeginContact(damageSource: Entity, damageTarget: Entity) {
        val (damageAmount,sourceEntity,isContinuous) = damageSource[Damage]
        if (sourceEntity == damageTarget) return
        damageTarget.configure {
            val damageTakenComp = it.getOrAdd(DamageTaken){ DamageTaken(0,isContinuous) }
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

    private fun handleItemBeginContact(itemEntity: Entity, playerEntity: Entity) {
        if (itemEntity.has(EntityTag.COLLECTED)) return
        val item = itemEntity.getOrNull(Item) ?: return
        val inventory = playerEntity.getOrNull(Inventory) ?: return
        inventory.addItem(item.itemData.also { it.owner = playerEntity })
        itemEntity.configure {
            it += EntityTag.COLLECTED
        }
    }

    private fun handleRangeAttackAndStaticObjectCollision(activeAttackEntity: Entity) {
        val attackMeta = activeAttackEntity[AttackMeta]
        attackMeta.collidedWithWall = true
    }

    private fun handleSensorAndPlayerCollision(playerEntity: Entity, aiEntity: Entity, isBeginContact : Boolean) {
        val aiComponent = aiEntity.getOrNull(AiComponent)?:return
        if (isBeginContact){
            aiComponent.nearbyEntities.add(playerEntity)
        }else{
            aiComponent.nearbyEntities.remove(playerEntity)
        }
    }

    // ----- /HANDLE COLLISIONS -----

    // ----- CHECK CONDITIONS -----
    private fun isDamageCollision(entityA: Entity, entityB: Entity, fixtureB: Fixture): Boolean {
        val attackMeta = entityA.getOrNull(AttackMeta)?:return false
        val owner = attackMeta.owner
        val isEnemyAndAllieCollision = (owner.has(EntityTag.ENEMY) && (entityB.has(EntityTag.ALLIE) || entityB.has(EntityTag.PLAYER)))
                || ((owner.has(EntityTag.ALLIE) || owner.has(EntityTag.PLAYER)) && entityB.has(EntityTag.ENEMY))
        return entityA has Damage && entityB has Life && fixtureB.isSensor && fixtureB.isHitbox && isEnemyAndAllieCollision
    }

    private fun isItemCollision(entityA: Entity, entityB: Entity, fixtureB: Fixture): Boolean {
        return entityA.has(Item) && entityB.has(EntityTag.PLAYER) && fixtureB.isSensor && !fixtureB.isSensorFixture
    }

    private fun isRangeAttackAndStaticObjectCollision(entityA: Entity?,fixtureA : Fixture,entityB: Entity?,fixtureB:Fixture) : Boolean{
        val attackMeta = entityA?.getOrNull(AttackMeta)?:return false
        if (attackMeta.owner == entityB) return false
        return  entityA has AttackMeta && fixtureA.isRangeAttackFixture  && !fixtureA.isSensor && !fixtureB.isSensor && fixtureB.isStaticBody
    }

    private fun isAttackFixtureCollision(entityA: Entity, entityB: Entity): Boolean {
        val attackMeta = entityA.getOrNull(AttackMeta) ?: return false
        val owner = attackMeta.owner
        return owner != entityB
    }

    private fun handleAttackFixtureCollision(
        entityA: Entity,
        entityB: Entity,
        fixtureA: Fixture,
        fixtureB: Fixture
    ) {
        val attackMeta = entityA[AttackMeta]
        when{
            fixtureA.isRangeAttackFixture -> handleRangeAttackFixtureCollision(entityA,entityB,fixtureA,fixtureB,attackMeta)
            fixtureA.isMeleeAttackFixture -> handleMeleeAttackFixtureCollision(entityA,entityB,fixtureA,fixtureB,attackMeta)
        }
    }

    private fun handleRangeAttackFixtureCollision(
        entityA: Entity,
        entityB: Entity,
        fixtureA: Fixture,
        fixtureB: Fixture,
        attackMeta: AttackMeta
    ) {
        when{
            isRangeAttackAndStaticObjectCollision(entityA, fixtureA, entityB, fixtureB) -> handleRangeAttackAndStaticObjectCollision(entityA)
        }
    }

    private fun handleMeleeAttackFixtureCollision(
        entityA: Entity,
        entityB: Entity,
        fixtureA: Fixture,
        fixtureB: Fixture,
        attackMeta: AttackMeta
    ) {
        if (isDamageCollision(entityA,entityB,fixtureB)){
            handleDamageBeginContact(entityA,entityB)
        }
        if (isKnockBackCollision(fixtureB)) {
            handleKnockbackBeginContact(entityA, entityB,fixtureA, fixtureB, attackMeta)
        }
    }

    private fun isKnockBackCollision(fixtureB: Fixture): Boolean {
        return fixtureB.body.type == BodyType.DynamicBody && !fixtureB.isSensor
    }

    private fun handleKnockbackBeginContact(
        entityA: Entity,
        entityB: Entity,
        fixtureA: Fixture,
        fixtureB: Fixture,
        attackMeta: AttackMeta
    ) {
        val owner = attackMeta.owner
        val attackType = attackMeta.attackMetaData.attackType
        val attackBodyPosition = owner[Graphic].center
        val targetBody = fixtureB.body
        val targetBodyPosition = entityB[Graphic].center
        val targetMass = targetBody.mass
        val direction = targetBodyPosition - attackBodyPosition
        val knockbackVec = direction.set(direction.x + if (direction.x < 0f) -attackType.knockbackVector.x else attackType.knockbackVector.x , direction.y + attackType.knockbackVector.y)
        val impulse = knockbackVec / targetMass
        println("targetmass $targetMass")
        targetBody.applyLinearImpulse(impulse, targetBody.worldCenter, true)
    }


    private fun isSensorAndPlayerCollision(entityA: Entity,fixtureA : Fixture,fixtureB:Fixture) : Boolean{
        return entityA has EntityTag.PLAYER && fixtureA.isHitbox && fixtureB.isSensorFixture
    }


    // -----/ CHECK CONDITIONS -----

    override fun beginContact(contact: Contact) {
        val fixtureA = contact.fixtureA
        val fixtureB = contact.fixtureB
        val entityA = fixtureA.entity
        val entityB = fixtureB.entity

        if (entityA == null || entityB == null) {
            when{
                isRangeAttackAndStaticObjectCollision(entityA,fixtureA,entityB,fixtureB) -> handleRangeAttackAndStaticObjectCollision(entityA!!)
                isRangeAttackAndStaticObjectCollision(entityB,fixtureB,entityB,fixtureA) -> handleRangeAttackAndStaticObjectCollision(entityB!!)
                else -> Unit
            }
            return
        }

        when {
            isSensorAndPlayerCollision(entityA,fixtureA,fixtureB) -> handleSensorAndPlayerCollision(entityA, entityB ,true)
            isSensorAndPlayerCollision(entityB,fixtureB,fixtureA) -> handleSensorAndPlayerCollision(entityB, entityA ,true)
            isItemCollision(entityA,entityB,fixtureB) -> handleItemBeginContact(entityA,entityB)
            isItemCollision(entityB,entityA,fixtureA) -> handleItemBeginContact(entityB,entityA)
            isAttackFixtureCollision(entityA,entityB) -> handleAttackFixtureCollision(entityA,entityB,fixtureA,fixtureB)
            isAttackFixtureCollision(entityB,entityA) -> handleAttackFixtureCollision(entityB,entityA,fixtureB,fixtureA)
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
            isSensorAndPlayerCollision(entityA,fixtureA,fixtureB) -> handleSensorAndPlayerCollision(entityA, entityB ,false)
            isSensorAndPlayerCollision(entityB,fixtureB,fixtureA) -> handleSensorAndPlayerCollision(entityB, entityA ,false)
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
        if (fixtureA.isRangeAttackFixture && fixtureB.isDynamicBody){
            contact.isEnabled = false
        }

    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {

    }


    override fun onEvent(event: GameEvent) {
        when(event){
            is GameEvent.ParticleEvent ->{
                val bodyPosition = event.owner[Physic].body.position
                val lowerXY = event.owner[Jump].lowerXY
                val jumpRectLowerXY = bodyPosition + lowerXY
                val position = vec2(jumpRectLowerXY.x - 0.75f,jumpRectLowerXY.y)
                world.entity{
                    it += Particle(event.particleType,event.owner)
                    it += Graphic(sprite(GameObject.DUST_PARTICLES.atlasKey, AnimationType.valueOf(event.particleType.name),position  , assetHelper ,0f))
                    it += Animation(GameObject.DUST_PARTICLES.atlasKey, animationData = AnimationData(
                        animationType = AnimationType.valueOf(event.particleType.name),
                        playMode = com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL
                    ))
                }
            }
            else -> Unit
        }
    }
}
