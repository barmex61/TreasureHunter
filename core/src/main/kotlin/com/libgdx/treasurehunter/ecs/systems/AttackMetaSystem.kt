package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.Shape
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
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.utils.mirrorVertices
import com.libgdx.treasurehunter.utils.playerSwordMeleeAttackVertices
import com.libgdx.treasurehunter.utils.playerSwordRangedAttackVertices
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

    fun createAttackFixture(keyFrameIndex : Int,attackBody : Body,attackType: AttackType,flipX : Boolean = false,isMelee: Boolean ,isSensor : Boolean){
        attackBody.fixtureList.forEach {
            attackBody.destroyFixture(it)
        }
        val shape = if (isMelee) getMeleeAttackShape(attackType,keyFrameIndex,flipX) else getRangedAttackShape(flipX)
        shape?:return
        val attackFixtureDef = FixtureDef().apply {
            this.isSensor = isSensor
            density = 0f
            restitution = 0f
            this.shape = shape
        }
        attackBody.createFixture(attackFixtureDef).apply {
            userData = if (isMelee) "meleeAttackFixture" else "rangeAttackFixture"
        }
    }

    private fun getMeleeAttackShape(attackType: AttackType,keyFrameIndex: Int,flipX: Boolean) : Shape? {
        val fixtureVertices =playerSwordMeleeAttackVertices[attackType]?.get(keyFrameIndex)
        fixtureVertices?:return null
        val mirroredVertices = if (flipX) fixtureVertices.mirrorVertices() else fixtureVertices
        return ChainShape().apply {
            createLoop(mirroredVertices)
        }
    }

    private fun getRangedAttackShape(flipX: Boolean): ChainShape {
        val originalShape = playerSwordRangedAttackVertices!!.first().fixtureDef.shape as ChainShape
        val vertexCount = originalShape.vertexCount
        val vertices = Array(vertexCount) { Vector2() }

        for (i in 0 until vertexCount) {
            val vertex = Vector2()
            originalShape.getVertex(i, vertex)
            vertices[i] = if (flipX) Vector2(-vertex.x + 0.65f, vertex.y) else Vector2(vertex.x , vertex.y)
        }

        return ChainShape().apply {
            createChain(vertices)
        }
    }

}
