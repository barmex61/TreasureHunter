package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.Shape
import com.badlogic.gdx.utils.Array
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.state.PlayerState
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.AttackMeta
import com.libgdx.treasurehunter.ecs.components.AttackType
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.ecs.systems.AnimationSystem.Companion.setFlipX
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.TextureAtlasAssets
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
import ktx.app.gdxError
import ktx.collections.isNotEmpty

class AttackMetaSystem(
    assetHelper: AssetHelper = inject()
) : IteratingSystem(
    family = family { all(AttackMeta, Physic) }
) {
    private val gameObjectAtlas = assetHelper[TextureAtlasAssets.GAMEOBJECT]

    override fun onTickEntity(entity: Entity) {
        val attackMeta = entity[AttackMeta]
        val (owner, currentFrameIndex, _, _, _, attackMetaData) = attackMeta
        val (body, _) = entity[Physic]
        val ownerFlipX = owner[Move].flipX
        val graphic = entity[Graphic]

        if (attackMetaData.isMelee) {
            handleMeleeAttack(entity, owner, ownerFlipX, currentFrameIndex, attackMeta, body, attackMetaData.attackType)
            attackMetaData.attackDestroyTime -= deltaTime

            if (attackMetaData.attackDestroyTime <= 0f) {
                GameEventDispatcher.fireEvent(GameEvent.RemoveEntityEvent(entity))
            }
            graphic.sprite.setAlpha(if (body.fixtureList.isEmpty) 0f else 1f)
        } else {
            handleRangedAttack(entity, owner, ownerFlipX, currentFrameIndex, attackMeta, body, attackMetaData.attackType)
        }
    }

    private fun handleMeleeAttack(
        entity: Entity,
        owner: Entity,
        ownerFlipX: Boolean,
        currentFrameIndex: Int,
        attackMeta: AttackMeta,
        body: Body,
        attackType: AttackType
    ) {
        val ownerAnimComp = owner[Animation]
        val ownerCenter = owner[Graphic].center
        val keyFrameIndex = ownerAnimComp.getAttackAnimKeyFrameIx()
        if (keyFrameIndex != -1 && (keyFrameIndex != currentFrameIndex || ownerFlipX != attackMeta.isFixtureMirrored)) {
            createAttackFixture(entity, keyFrameIndex, body, attackType, ownerFlipX, isMelee = true, isSensor = true)
            attackMeta.currentFrameIndex = keyFrameIndex
            attackMeta.isFixtureMirrored = ownerFlipX
        }

        if (keyFrameIndex == -1 && body.fixtureList.isNotEmpty()) {
            body.destroyFixtures()
        }

        body.setTransform(ownerCenter, body.angle)
    }

    private fun handleRangedAttack(
        entity: Entity,
        owner: Entity,
        ownerFlipX: Boolean,
        currentFrameIndex: Int,
        attackMeta: AttackMeta,
        body: Body,
        attackType: AttackType
    ) {
        val keyFrameIx = owner[Animation].getAttackAnimKeyFrameIx()
        val animType = owner[Animation].animationData.animationType

        if (keyFrameIx == 1 && !attackMeta.hasFixture) {
            setRangedAttackFixture(entity, owner, ownerFlipX, currentFrameIndex, attackMeta, body, attackType)
            attackMeta.isFixtureMirrored = ownerFlipX
        }

        if (keyFrameIx == 2 && animType == AnimationType.THROW) {
            owner[State].stateMachine.changeState(PlayerState.SWORD_THROWED)
        }
    }

    private fun applyForceToAttackBody(ownerFlipX: Boolean, ownerCenter: Vector2, body: Body) {
        val (multiplier, offset) = if (!ownerFlipX) 1f to Vector2(0f, 0f) else -1f to vec2(-0.5f, 0f)
        val pos = ownerCenter + offset
        body.setTransform(pos, body.angle)
        body.applyLinearImpulse(Vector2(7f * multiplier, 0f), body.position, true)
    }

    private fun setRangedAttackFixture(
        entity: Entity,
        owner: Entity,
        ownerFlipX: Boolean,
        currentFrameIndex: Int,
        attackMeta: AttackMeta,
        body: Body,
        attackType: AttackType
    ) {
        val (sprite, _) = entity[Graphic]
        val ownerCenter = owner[Graphic].center
        sprite.setAlpha(1f)
        createAttackFixture(entity, currentFrameIndex, body, attackType, ownerFlipX, isMelee = false, isSensor = false)
        attackMeta.hasFixture = true
        applyForceToAttackBody(ownerFlipX, ownerCenter, body)
    }

    fun createAttackFixture(
        entity: Entity,
        keyFrameIndex: Int,
        attackBody: Body,
        attackType: AttackType,
        flipX: Boolean = false,
        isMelee: Boolean,
        isSensor: Boolean
    ) {
        attackBody.destroyFixtures()

        val shape = if (isMelee) {
            getMeleeAttackShape(attackType, keyFrameIndex, flipX)
        } else {
            getRangedAttackShape(flipX)
        }

        val fixtureDefs = mutableListOf<FixtureDefUserData>()
        shape?.let {
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
                val originalShapeWithOffset = originalShape.offset(vec2(0.5f, -0.5f))
                val newShape = if (flipX) mirrorChainShape(originalShapeWithOffset, vec2(0f, 0f)) else originalShapeWithOffset
                setMeleeAttackEffectSprite(entity, keyFrameIndex, newShape, attackType, flipX)

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

    private fun getMeleeAttackShape(attackType: AttackType, keyFrameIndex: Int, flipX: Boolean): Shape? {
        val fixtureVertices = playerSwordMeleeAttackVertices[attackType]?.get(keyFrameIndex) ?: return null
        val mirroredVertices = if (flipX) fixtureVertices.mirrorVertices() else fixtureVertices
        return ChainShape().apply { createLoop(mirroredVertices) }
    }

    private fun getRangedAttackShape(flipX: Boolean): ChainShape {
        val originalShape = playerSwordRangedAttackVertices!!.first().fixtureDef.shape as ChainShape
        return if (flipX) mirrorChainShape(originalShape, vec2(0.65f, 0f)) else originalShape
    }

    private fun setMeleeAttackEffectSprite(
        entity: Entity,
        keyFrameIndex: Int,
        newShape: ChainShape,
        attackType: AttackType,
        flipX: Boolean
    ) {
        val effectPositionCenter = newShape.calculateEffectPosition()
        val graphic = entity[Graphic]
        val textureRegion = getTextureRegion(GameObject.ATTACK_EFFECT, attackType, keyFrameIndex)
        graphic.sprite.setRegion(textureRegion)
        setFlipX(entity[AttackMeta].owner.getOrNull(Move), graphic.sprite)
        graphic.effectOffset = vec2(0f, 0f)
        val newOffsetX = (if (flipX) effectPositionCenter.x - graphic.sprite.width / 1.3f else effectPositionCenter.x - graphic.sprite.width / 2f).coerceAtMost(0.5f)
        val newOffsetY = (effectPositionCenter.y - graphic.sprite.height / 2.5f).coerceAtMost(-0.3f)
        graphic.effectOffset = vec2(newOffsetX, newOffsetY)
    }

    private val regionsCache = mutableMapOf<String, Array<TextureAtlas.AtlasRegion>>()

    fun getTextureRegion(
        gameObject: GameObject,
        attackType: AttackType,
        keyFrameIndex: Int
    ): TextureRegion {
        val atlasKey = "${gameObject.atlasKey}/${attackType.name.lowercase()}"
        return regionsCache.getOrPut(atlasKey) {
            gameObjectAtlas.findRegions(atlasKey) ?: gdxError("No regions for animation $atlasKey")
        }.get(keyFrameIndex)
    }
}

sealed interface AttackHandler {
    fun handleAttackFixture()
    fun handleAttackEffect()
}

class MeleeAttackHandler() : AttackHandler {

    override fun handleAttackFixture() {

    }

    override fun handleAttackEffect() {

    }
}

class RangeAttackHandler() : AttackHandler {

    override fun handleAttackFixture() {

    }

    override fun handleAttackEffect() {

    }
}
