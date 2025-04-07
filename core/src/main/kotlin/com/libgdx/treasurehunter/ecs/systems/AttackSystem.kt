package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.AttackState
import com.libgdx.treasurehunter.ecs.components.AttackType
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.utils.mirrorVertices
import com.libgdx.treasurehunter.utils.playerAttackVertices

import ktx.math.vec2

class AttackSystem(
    private val physicWorld : PhysicWorld = inject()
) : IteratingSystem(
    family = family { all(Attack, Physic, Graphic) }
) {

    override fun onTickEntity(entity: Entity) {
        val attackComp = entity[Attack]
        val animComp = entity[Animation]
        val (_,_,center) = entity[Graphic]
        val (attackDamage, wantsToAttack, attackState,attackBody,attackDuration,attackType,currentAttackKeyFrameIx) = attackComp
        when(attackState){
            AttackState.READY -> {
                if (wantsToAttack) {
                    attackComp.attackBody = createAttackBody(center,entity)
                    attackComp.attackState = AttackState.ATTACKING
                }
            }
            AttackState.ATTACKING -> {
                attackBody?.let {
                    // ---- Current attack fixture set up -----
                    val keyFrameIndex = animComp.getAttackAnimKeyFrameIx()
                    val flipX = entity.getOrNull(Move)?.flipX == true
                    if ((keyFrameIndex != currentAttackKeyFrameIx) || (flipX != attackComp.isAttackFixtureMirrored)) {
                        createAttackFixture(keyFrameIndex,attackBody,attackType,flipX)
                        attackComp.currentAttackAnimKeyFrameIx = keyFrameIndex
                        attackComp.isAttackFixtureMirrored = flipX
                    }

                    // --- Sync attack body position with graphic component ----
                    attackBody.setTransform(center, attackBody.angle)
                }
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

    private fun createAttackBody(centerPosition : Vector2,attackerEntity: Entity): Body {
        val attackBody = physicWorld.createBody(BodyDef().apply {
            type = BodyDef.BodyType.StaticBody
            position.set(centerPosition.x,centerPosition.y )
            fixedRotation = true
            gravityScale = 0f

        }).apply { userData =  attackerEntity }
        return attackBody
    }

    private fun createAttackFixture(keyFrameIndex : Int,attackBody : Body,attackType: AttackType,flipX : Boolean = false){
        attackBody.fixtureList.forEach {
            attackBody.destroyFixture(it)
        }
        if (keyFrameIndex == -1) return
        val fixtureVertices = playerAttackVertices[attackType]?.get(keyFrameIndex)?:return
        val mirroredVertices = if (flipX) fixtureVertices.mirrorVertices() else fixtureVertices
        val attackFixtureDef = FixtureDef().apply {
            isSensor = true
            density = 0f
            friction = 0f
            restitution = 0f
            shape = ChainShape().apply {
                createLoop(mirroredVertices)
            }
        }
        attackBody.createFixture(attackFixtureDef)
    }

}

