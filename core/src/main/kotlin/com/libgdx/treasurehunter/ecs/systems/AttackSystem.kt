package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.AttackState
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.utils.Constants.UNIT_SCALE
import com.libgdx.treasurehunter.utils.createBody
import ktx.math.vec2

class AttackSystem(
    private val physicWorld : PhysicWorld = inject()
) : IteratingSystem(
    family = family { all(Attack, Physic) }
) {

    override fun onTickEntity(entity: Entity) {
        val attackComp = entity[Attack]
        val physicComp = entity[Physic]
        val entityPosition = physicComp.body.position

        val (attackDamage, wantsToAttack, attackState,attackBody) = attackComp
        when(attackState){
            AttackState.READY -> {
                if (wantsToAttack) {
                    attackComp.attackBody = createAttackBody(entityPosition, attackDamage)
                    attackComp.attackState = AttackState.ATTACKING
                }
            }
            AttackState.ATTACKING -> {
                attackComp.attackDuration -= deltaTime
                if (attackComp.attackDuration <= 0f) {
                    attackComp.attackState = AttackState.DONE
                }
            }
            AttackState.DONE -> {
                resetAttackComp(attackComp)
            }}
        }

    private fun resetAttackComp(attackComp: Attack) {
        attackComp.attackBody?.let { body ->
            physicWorld.destroyBody(body)
            attackComp.attackBody = null
        }
        attackComp.attackState = AttackState.READY
        attackComp.wantsToAttack = false
        attackComp.attackDuration = 1f
    }

    private fun createAttackBody(entityPosition: Vector2, attackDamage: Float): Body {
        val attackBody = physicWorld.createBody(BodyDef().apply {
            type = BodyDef.BodyType.DynamicBody
            position.set(entityPosition.x,entityPosition.y )
            fixedRotation = true
            gravityScale = 1f
        })
        println("attackBody ${attackBody.position}")
        attackBody.userData = attackDamage
        var shape = PolygonShape()
        val fixtureDef = FixtureDef().apply {
            isSensor = true
            this.shape = shape.apply {
                setAsBox(1f,1f,vec2(1f,1f),0f)
            }
        }
        attackBody.createFixture(fixtureDef)
        shape.dispose()
        return attackBody
    }
}

