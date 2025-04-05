package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.AttackState
import com.libgdx.treasurehunter.ecs.components.AttackType
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.utils.playerAttackVertices

import ktx.math.vec2

class AttackSystem(
    private val physicWorld : PhysicWorld = inject()
) : IteratingSystem(
    family = family { all(Attack, Physic, Graphic) }
) {

    override fun onTickEntity(entity: Entity) {
        val attackComp = entity[Attack]

        val (attackDamage, wantsToAttack, attackState,attackBody,attackDuration,attackType) = attackComp
        when(attackState){
            AttackState.READY -> {
                if (wantsToAttack) {
                    attackComp.attackBody = createAttackBody(entity,attackType)
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
    companion object{
        val DEBUG_RECT = Rectangle(0f,0f,1f,1f)
    }
    private fun createAttackBody(attackerEntity: Entity, attackType: AttackType): Body {
        val physicComp = attackerEntity[Physic]
        val graphicComp = attackerEntity[Graphic]
        val centerPosition = vec2(physicComp.body.position.x + graphicComp.sprite.width/2f,physicComp.body.position.y + graphicComp.sprite.height/2f)
        val attackBody = physicWorld.createBody(BodyDef().apply {
            type = BodyDef.BodyType.DynamicBody
            position.set(centerPosition.x,centerPosition.y )
            fixedRotation = true
            gravityScale = 0f

        }).apply { userData =  attackerEntity }
        DEBUG_RECT.set(centerPosition.x,centerPosition.y,0.1f,0.1f)
        var shape = ChainShape()
        val fixtureDef = FixtureDef().apply {
            isSensor = true
            this.shape = shape.apply {
                this.createLoop(
                    playerAttackVertices[attackType]
                )
            }
        }
        attackBody.createFixture(fixtureDef)
        shape.dispose()
        return attackBody
    }
}

