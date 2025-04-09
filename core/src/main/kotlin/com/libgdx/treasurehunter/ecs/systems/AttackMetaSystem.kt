package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ai.PlayerState
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AttackMeta
import com.libgdx.treasurehunter.ecs.components.AttackType
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.ecs.systems.DebugSystem.Companion.JUMP_DEBUG_RECT
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.utils.mirrorVertices
import com.libgdx.treasurehunter.utils.playerMeleeAttackVertices
import com.libgdx.treasurehunter.utils.playerRangeAttackVertices
import com.libgdx.treasurehunter.utils.plus
import ktx.math.vec2

class AttackMetaSystem : IteratingSystem(
    family = family { all(AttackMeta, Physic) }
) {


    override fun onTickEntity(entity: Entity) {
        val attackMeta = entity[AttackMeta]
        var (isMelee,owner, attackType, currentFrameIndex, isFixtureMirrored,hasFixture,attackCooldown) = attackMeta
        val (body,_) = entity[Physic]
        val ownerFlipX = owner[Move].flipX
        if (isMelee){
            setMeleeAttackFixtures(owner,ownerFlipX,currentFrameIndex,attackMeta,body,attackType)
            attackCooldown -= deltaTime
            attackMeta.attackDestroyCooldown = attackCooldown
            if (attackCooldown <= 0f){
                GameEventDispatcher.fireEvent(GameEvent.RemoveEntityEvent(entity))
            }
        }else{
            val keyFrameIx = owner[Animation].getAttackAnimKeyFrameIx()
            if (keyFrameIx == 1 && !hasFixture){
                setRangedAttackFixture(entity,owner,ownerFlipX,currentFrameIndex,attackMeta,body,attackType)
                attackMeta.isFixtureMirrored = ownerFlipX
                owner[State].stateMachine.changeState(PlayerState.SWORD_THROWED)
            }
        }

    }

    private fun setMeleeAttackFixtures(owner: Entity,ownerFlipX: Boolean,currentFrameIndex: Int,attackMeta: AttackMeta,body: Body,attackType: AttackType){
        val ownerAnimComp = owner[Animation]
        val ownerCenter = owner[Graphic].center
        val keyFrameIndex = ownerAnimComp.getAttackAnimKeyFrameIx()
        if (keyFrameIndex != -1 && ((keyFrameIndex != currentFrameIndex) || (ownerFlipX != attackMeta.isFixtureMirrored))) {
            createAttackFixture(keyFrameIndex,body,attackType,ownerFlipX,true,true)
            attackMeta.currentFrameIndex = keyFrameIndex
            attackMeta.isFixtureMirrored = ownerFlipX
        }
        body.setTransform(ownerCenter, body.angle)
    }

    private fun setRangedAttackFixture(entity: Entity, owner: Entity, ownerFlipX: Boolean, currentFrameIndex: Int, attackMeta: AttackMeta, body: Body, attackType: AttackType){
        val (sprite,_) = entity[Graphic]
        val ownerCenter = owner[Graphic].center
        sprite.setAlpha(1f)
        createAttackFixture(currentFrameIndex,body,attackType,ownerFlipX,false,false)
        attackMeta.hasFixture = true
        applyForceToAttackBody(ownerFlipX,ownerCenter,body)
    }

    private fun applyForceToAttackBody(ownerFlipX: Boolean,ownerCenter : Vector2,body: Body){
        val (multiplayer,offset) = when {
            ownerFlipX == false -> {
                1f to Vector2(0f,0f)
            }
            else -> {
                -1f to vec2(-0.5f,0f)
            }
        }
        val pos = ownerCenter + offset
        body.setTransform(pos, body.angle)
        body.applyLinearImpulse(Vector2(7f * multiplayer,0f)  ,body.position, true)
    }

    companion object{
        fun createAttackFixture(keyFrameIndex : Int,attackBody : Body,attackType: AttackType,flipX : Boolean = false,isMelee: Boolean ,isSensor : Boolean){
            attackBody.fixtureList.forEach {
                attackBody.destroyFixture(it)
            }
            val fixtureVertices =if (isMelee) playerMeleeAttackVertices[attackType]?.get(keyFrameIndex) else playerRangeAttackVertices
            fixtureVertices?:return
            val mirroredVertices = if (flipX) fixtureVertices.mirrorVertices() else fixtureVertices
            val attackFixtureDef = FixtureDef().apply {
                this.isSensor = isSensor
                density = 0f
                friction = 0f
                restitution = 0f
                shape = ChainShape().apply {
                    createLoop(mirroredVertices)
                }
            }
            attackBody.createFixture(attackFixtureDef).apply {
                userData = if (isMelee) "meleeAttackFixture" else "rangeAttackFixture"
            }
        }
    }

}
