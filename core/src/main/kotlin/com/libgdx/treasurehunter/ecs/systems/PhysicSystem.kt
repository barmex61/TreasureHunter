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
import com.libgdx.treasurehunter.ecs.components.Damage
import com.libgdx.treasurehunter.ecs.components.DamageTaken
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Jump
import com.libgdx.treasurehunter.ecs.components.Life
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.game.PhysicWorld
import ktx.math.component1
import ktx.math.component2
import kotlin.compareTo
import kotlin.math.abs

class PhysicSystem (
    private val physicWorld : PhysicWorld = inject()
): IteratingSystem(
    family = family{all(Physic, Graphic)},
    interval = Fixed(1/300f)
) , ContactListener{

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
            body.setLinearVelocity(moveComp.currentSpeed , body.linearVelocity.y)
        }
    }

    override fun onAlphaEntity(entity: Entity, alpha: Float) {
        val (sprite) = entity[Graphic]
        val (body,previousPosition) = entity[Physic]
        val (prevX,prevY) = previousPosition
        val (bodyX,bodyY) = body.position
        sprite.setPosition(
            MathUtils.lerp(prevX,bodyX,alpha),
            MathUtils.lerp(prevY,bodyY,alpha)
        )
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


    private fun handleDamageBeginContact(damageSource: Entity, damageTarget: Entity) = with(world){
        val (damageAmount) = damageSource[Damage]
        damageTarget.configure {
            val damageTakenComp = it.getOrAdd(DamageTaken){ DamageTaken(0) }
            damageTakenComp.damageAmount = (damageTakenComp.damageAmount + damageAmount).coerceAtMost(damageAmount)
        }
    }

    private fun handleDamageEndContact(damageSource: Entity, damageTarget: Entity) = with(world){
        damageTarget.getOrNull(DamageTaken)?.let {
            val (damageAmount) = damageSource[Damage]
            it.damageAmount -= damageAmount
            if (it.damageAmount <= 0){
                damageTarget.configure { it -= DamageTaken }
            }
        }
    }

    private fun isDamageCollision(entityA: Entity,entityB: Entity,fixtureA: Fixture,fixtureB:Fixture) : Boolean{
        return  entityA has Damage && entityB has Life && fixtureB.isSensor && fixtureA.isHitbox && fixtureB.isHitbox
    }

    override fun beginContact(contact: Contact) {
        val fixtureA = contact.fixtureA
        val fixtureB = contact.fixtureB
        val entityA = fixtureA.entity
        val entityB = fixtureB.entity
        if (entityA == null || entityB == null) return
        when {
            isDamageCollision(entityA,entityB,fixtureA,fixtureB) -> handleDamageBeginContact(entityA,entityB)
            isDamageCollision(entityB,entityA,fixtureB,fixtureA) -> handleDamageBeginContact(entityB,entityA)
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
            isDamageCollision(entityA,entityB,fixtureA,fixtureB) ->  handleDamageEndContact(entityA,entityB)
            isDamageCollision(entityB,entityA,fixtureB,fixtureA) -> handleDamageEndContact(entityB,entityA)
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
}
