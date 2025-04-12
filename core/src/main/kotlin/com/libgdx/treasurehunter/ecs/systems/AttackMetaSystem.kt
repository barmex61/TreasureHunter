package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.Shape
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ai.PlayerState
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.AttackMeta
import com.libgdx.treasurehunter.ecs.components.AttackType
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.ecs.systems.DebugSystem.Companion.JUMP_DEBUG_RECT
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.utils.Constants.OBJECT_FIXTURES
import com.libgdx.treasurehunter.utils.FixtureDefUserData
import com.libgdx.treasurehunter.utils.GameObject
import com.libgdx.treasurehunter.utils.copy
import com.libgdx.treasurehunter.utils.createFixtures
import com.libgdx.treasurehunter.utils.destroyFixtures
import com.libgdx.treasurehunter.utils.mirrorChainShape
import com.libgdx.treasurehunter.utils.mirrorVertices
import com.libgdx.treasurehunter.utils.offset
import com.libgdx.treasurehunter.utils.playerSwordMeleeAttackVertices
import com.libgdx.treasurehunter.utils.playerSwordRangedAttackVertices
import com.libgdx.treasurehunter.utils.plus
import ktx.math.vec2
import com.libgdx.treasurehunter.utils.calculateEffectPosition
class AttackMetaSystem : IteratingSystem(
    family = family { all(AttackMeta, Physic) }
) {

    var keyframes = -2
    override fun onTickEntity(entity: Entity) {
        val attackMeta = entity[AttackMeta]
        var (isMelee,owner, attackType, currentFrameIndex, isFixtureMirrored,hasFixture,attackCooldown) = attackMeta
        val (body,_) = entity[Physic]
        val ownerFlipX = owner[Move].flipX
        val graphic = entity[Graphic]
        if (isMelee){
            setMeleeAttackFixtures(entity,owner,ownerFlipX,currentFrameIndex,attackMeta,body,attackType)
            attackCooldown -= deltaTime
            attackMeta.attackDestroyCooldown = attackCooldown
            if (attackCooldown <= 0f){
                GameEventDispatcher.fireEvent(GameEvent.RemoveEntityEvent(entity))
            }
            if (body.fixtureList.size == 0) graphic.sprite.setAlpha(0f) else graphic.sprite.setAlpha(1f)
        }else{

            val keyFrameIx = owner[Animation].getAttackAnimKeyFrameIx()
            val animType = owner[Animation].animationData.animationType
            if (keyFrameIx == 1 && !hasFixture){
                setRangedAttackFixture(entity,owner,ownerFlipX,currentFrameIndex,attackMeta,body,attackType)
                attackMeta.isFixtureMirrored = ownerFlipX
            }
            if (keyFrameIx == 2 && animType == AnimationType.THROW){
                owner[State].stateMachine.changeState(PlayerState.SWORD_THROWED)
            }
        }

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


    private fun setMeleeAttackFixtures(entity: Entity,owner: Entity,ownerFlipX: Boolean,currentFrameIndex: Int,attackMeta: AttackMeta,body: Body,attackType: AttackType){
        val ownerAnimComp = owner[Animation]
        val ownerCenter = owner[Graphic].center
        val keyFrameIndex = ownerAnimComp.getAttackAnimKeyFrameIx()
        if (keyFrameIndex != -1 && ((keyFrameIndex != currentFrameIndex) || (ownerFlipX != attackMeta.isFixtureMirrored))) {
            createAttackFixture(entity,keyFrameIndex,body,attackType,ownerFlipX,true,true)
            attackMeta.currentFrameIndex = keyFrameIndex
            attackMeta.isFixtureMirrored = ownerFlipX
        }
        if (keyFrameIndex == -1 && body.fixtureList.size > 0){
            body.destroyFixtures()
        }
        body.setTransform(ownerCenter, body.angle)
    }

    private fun setRangedAttackFixture(entity: Entity, owner: Entity, ownerFlipX: Boolean, currentFrameIndex: Int, attackMeta: AttackMeta, body: Body, attackType: AttackType){
        val (sprite,_) = entity[Graphic]
        val ownerCenter = owner[Graphic].center
        sprite.setAlpha(1f)
        createAttackFixture(entity,currentFrameIndex,body,attackType,ownerFlipX,false,false)
        attackMeta.hasFixture = true
        applyForceToAttackBody(ownerFlipX,ownerCenter,body)
    }


    fun createAttackFixture(entity: Entity,keyFrameIndex: Int, attackBody: Body, attackType: AttackType, flipX: Boolean = false, isMelee: Boolean, isSensor: Boolean) {
        attackBody.destroyFixtures()
        val shape = if (isMelee) {
            getMeleeAttackShape(attackType, keyFrameIndex, flipX)
        } else {
            getRangedAttackShape(flipX)
        }

        val fixtureDefs = mutableListOf<FixtureDefUserData>()
        if (shape != null){
            fixtureDefs.add(
                FixtureDefUserData(
                    fixtureDef = FixtureDef().apply {
                        this.isSensor = isSensor
                        density = 0f
                        restitution = 0f
                        this.shape = shape
                    },
                    userData = if (isMelee) "meleeAttackFixture" else "rangeAttackFixture"
                )
            )
        }

        if (isMelee) {
            val attackEffectFixture = OBJECT_FIXTURES[GameObject.valueOf(attackType.name)]
            attackEffectFixture?.getOrNull(keyFrameIndex)?.let { originalFixtureData ->
                val originalShape = originalFixtureData.fixtureDef.shape as? ChainShape ?: return@let
                val originalShapeWithOffset = originalShape.offset(vec2(0.5f,-0.5f))
                val newShape = if (flipX) {
                     mirrorChainShape(originalShapeWithOffset, vec2(0f,0f))
                } else {
                    originalShapeWithOffset
                }
                val effectPositionCenter = newShape.calculateEffectPosition()
                val graphic = entity[Graphic]
                println(effectPositionCenter)
                JUMP_DEBUG_RECT.set(effectPositionCenter.x,effectPositionCenter.y,0.5f,0.5f)
                graphic.effectOffset = vec2(effectPositionCenter.x - 0.3f - graphic.sprite.width/2f ,effectPositionCenter.y + 0.2f - graphic.sprite.height/2f)
                fixtureDefs.add(
                    FixtureDefUserData(
                        fixtureDef = originalFixtureData.fixtureDef.copy().apply { this.shape = newShape },
                        userData = originalFixtureData.userData
                    )
                )
            }
        }

        attackBody.createFixtures(fixtureDefs)
        fixtureDefs.clear()
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
        val finalShape = if (flipX) mirrorChainShape(originalShape,vec2(0.65f,0f)) else originalShape
        return finalShape
    }



}
