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
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Jump
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.game.PhysicWorld
import ktx.math.component1
import ktx.math.component2
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

    private val Fixture.isPlayerFoot : Boolean
        get() = this.userData == "footFixture"

    private val Fixture.isGround : Boolean
        get() = this.userData == "ground"

    private val Fixture.entity : Entity?
        get() {
            val userData = this.body.userData
            return userData as? Entity
        }

    private fun isPlayerFootAndGroundCollision(fixtureA: Fixture,fixtureB: Fixture ) : Boolean {
        return fixtureA.isPlayerFoot && fixtureB.isGround
    }

    private fun handlePlayerFootAndGroundContact(normalY : Float,playerEntity: Entity){
        if (abs(normalY) <= 0.5f) return
        playerEntity[Jump].canJump = true
    }


    override fun beginContact(contact: Contact) {
        val fixtureA = contact.fixtureA
        val fixtureB = contact.fixtureB
        val entityA = fixtureA.entity
        val entityB = fixtureB.entity

        if (entityA == null || entityB == null){
            when{
                isPlayerFootAndGroundCollision(fixtureA,fixtureB) && entityA != null -> handlePlayerFootAndGroundContact(contact.worldManifold.normal.y,entityA)
                isPlayerFootAndGroundCollision(fixtureB,fixtureA) && entityB != null-> handlePlayerFootAndGroundContact(contact.worldManifold.normal.y,entityB)
            }
            return
        }

    }

    override fun endContact(contact: Contact) {
    }

    override fun preSolve(contact: Contact, oldManifold: Manifold) {
    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {

    }
}
