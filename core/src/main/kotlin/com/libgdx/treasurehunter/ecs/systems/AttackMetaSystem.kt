package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.utils.Array
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.AttackMeta
import com.libgdx.treasurehunter.ecs.components.AttackMetaData
import com.libgdx.treasurehunter.ecs.components.AttackType
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Item
import com.libgdx.treasurehunter.ecs.components.ItemType
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Physic
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
import com.libgdx.treasurehunter.utils.mirrorVertices
import com.libgdx.treasurehunter.utils.offset
import ktx.math.vec2
import com.libgdx.treasurehunter.utils.calculateEffectPosition
import com.libgdx.treasurehunter.utils.mirror
import ktx.app.gdxError
import ktx.collections.isNotEmpty


class AttackMetaSystem(
    private val assetHelper: AssetHelper = inject()
) : IteratingSystem(
    family = family { all(AttackMeta, Physic) }
) {

    override fun onTickEntity(entity: Entity) {
        val attackMeta = entity[AttackMeta]
        val (owner, currentFrameIndex, _, _, _, attackMetaData) = attackMeta
        val (body, _) = entity[Physic]
        val graphic = entity[Graphic]
        val ownerFlipX = owner[Move].flipX
        val ownerAnim = owner[Animation]
        val ownerGraphic = owner[Graphic]
        val ownerItem = owner.getOrNull(Item)
        val ownerCenter = ownerGraphic.center
        if (attackMeta.attackHandler.gameObjectAtlas == null){
            attackMeta.attackHandler.gameObjectAtlas = assetHelper[TextureAtlasAssets.GAMEOBJECT]
        }
        attackMeta.attackHandler.update(
            attackMetaData = attackMetaData,
            deltaTime = deltaTime,
            entity = entity,
            graphic = graphic,
            body = body
        )
        attackMeta.attackHandler.handleAttack(
            entity = entity,
            entityGraphic = graphic,
            owner = owner,
            ownerItem = ownerItem,
            ownerAnim = ownerAnim,
            ownerGraphic = ownerGraphic,
            ownerCenter = ownerCenter,
            ownerFlipX = ownerFlipX,
            currentFrameIndex = currentFrameIndex,
            attackMeta = attackMeta,
            body = body,
            attackType = attackMetaData.attackType
        )
    }
}

sealed interface AttackHandler {
    var gameObjectAtlas: TextureAtlas?

    fun update(attackMetaData: AttackMetaData, deltaTime: Float, entity: Entity, graphic: Graphic, body: Body) {
        attackMetaData.attackDestroyTime -= deltaTime
        if (attackMetaData.attackDestroyTime <= 0f) {
            GameEventDispatcher.fireEvent(GameEvent.RemoveEntityEvent(entity))
            attackMetaData.attackDestroyTime = attackMetaData.baseAttackDestroyTime
        }
        graphic.sprite.setAlpha(if (body.fixtureList.isEmpty) 0f else 1f)
    }

    fun handleAttack(
        entity: Entity,
        entityGraphic: Graphic,
        owner: Entity,
        ownerItem : Item?,
        ownerAnim: Animation,
        ownerGraphic: Graphic,
        ownerCenter: Vector2,
        ownerFlipX: Boolean,
        currentFrameIndex: Int,
        attackMeta: AttackMeta,
        body: Body,
        attackType: AttackType
    )
}

abstract class BaseAttackHandler : AttackHandler {
    override var gameObjectAtlas: TextureAtlas? = null

    protected val regionsCache = mutableMapOf<String, Array<TextureAtlas.AtlasRegion>>()

    protected fun getTextureRegion(gameObject: GameObject, attackType: AttackType, keyFrameIndex: Int): TextureRegion {
        val atlasKey = "${gameObject.atlasKey}/${attackType.name.lowercase()}"
        return regionsCache.getOrPut(atlasKey) {
            gameObjectAtlas?.findRegions(atlasKey) ?: gdxError("No regions for animation $atlasKey")
        }.get(keyFrameIndex)
    }


    protected fun createLoopFixture(vertices: FloatArray, userData: String,isSensor: Boolean): FixtureDefUserData {
        val shape = ChainShape().apply { this.createLoop(vertices) }
        return FixtureDefUserData(
            fixtureDef = FixtureDef().apply {
                this.isSensor = isSensor
                density = 0f
                restitution = 0f
                this.shape = shape
            },
            userData = userData
        )
    }
    protected fun createLoopFixture(chainShape: ChainShape, userData: String,isSensor : Boolean): FixtureDefUserData {
        val shape = chainShape
        return FixtureDefUserData(
            fixtureDef = FixtureDef().apply {
                this.isSensor = isSensor
                density = 0f
                restitution = 0f
                this.shape = shape
            },
            userData = userData
        )
    }
}

class MeleeAttackHandler : BaseAttackHandler() {

    override fun handleAttack(
        entity: Entity,
        entityGraphic: Graphic,
        owner: Entity,
        ownerItem: Item?,
        ownerAnim: Animation,
        ownerGraphic: Graphic,
        ownerCenter: Vector2,
        ownerFlipX: Boolean,
        currentFrameIndex: Int,
        attackMeta: AttackMeta,
        body: Body,
        attackType: AttackType
    ) {
        val keyFrameIndex = ownerAnim.getAttackAnimKeyFrameIx()
        if (keyFrameIndex != -1 && (keyFrameIndex != currentFrameIndex || ownerFlipX != attackMeta.isFixtureMirrored)) {
            attackMeta.currentFrameIndex = keyFrameIndex
            attackMeta.isFixtureMirrored = ownerFlipX
            val vertices = attackMeta.attackMetaData.meleeAttackFixtureVertices[keyFrameIndex]?.let {
                if (ownerFlipX) it.mirrorVertices() else it
            }
            body.destroyFixtures()
            val fixtureDefList = mutableListOf<FixtureDefUserData>()
            val attackFixture = vertices?.let { createLoopFixture(it, "meleeAttackFixture",true) }
            val effectFixture = createEffectFixture(keyFrameIndex, attackType, ownerFlipX, entityGraphic) ?: return
            if (attackFixture != null) fixtureDefList.add(attackFixture)
            fixtureDefList.add(effectFixture)
            body.createFixtures(fixtureDefList)
            fixtureDefList.clear()
        }

        if (keyFrameIndex == -1 && body.fixtureList.isNotEmpty()) {
            body.destroyFixtures()
        }

        body.setTransform(ownerCenter, body.angle)
    }

    private fun createEffectFixture(
        keyFrameIndex: Int,
        attackType: AttackType,
        ownerFlipX: Boolean,
        graphic: Graphic
    ): FixtureDefUserData? {
        val effectData = OBJECT_FIXTURES[GameObject.valueOf(attackType.name)]?.getOrNull(keyFrameIndex) ?: return null
        val shape = (effectData.fixtureDef.shape as? ChainShape)?.offset(vec2(0.5f, -0.5f)) ?: return null
        val newShape = if (ownerFlipX) shape.mirror(vec2(0f, 0f)) else shape
        val effectPosition = newShape.calculateEffectPosition()
        val region = getTextureRegion(GameObject.ATTACK_EFFECT, attackType, keyFrameIndex)

        graphic.sprite.setRegion(region)
        setFlipX(ownerFlipX, graphic.sprite)
        val newOffsetX = (if (ownerFlipX) effectPosition.x - graphic.sprite.width / 1.3f else effectPosition.x - graphic.sprite.width / 2f).coerceAtMost(0.5f)
        val newOffsetY = (effectPosition.y - graphic.sprite.height / 2.5f).coerceAtMost(-0.3f)
        graphic.effectOffset = vec2(newOffsetX, newOffsetY)

        return FixtureDefUserData(
            fixtureDef = effectData.fixtureDef.copy().apply { this.shape = newShape },
            userData = effectData.userData
        )
    }
}

class RangeAttackHandler : BaseAttackHandler() {

    override fun handleAttack(
        entity: Entity,
        entityGraphic: Graphic,
        owner: Entity,
        ownerItem: Item?,
        ownerAnim: Animation,
        ownerGraphic: Graphic,
        ownerCenter: Vector2,
        ownerFlipX: Boolean,
        currentFrameIndex: Int,
        attackMeta: AttackMeta,
        body: Body,
        attackType: AttackType
    ) {
        val keyFrameIx = ownerAnim.getAttackAnimKeyFrameIx()
        val animType = ownerAnim.animationData.animationType

        if (keyFrameIx == 1 && !attackMeta.hasFixture) {
            val chainShape = attackMeta.attackMetaData.rangeAttackFixtureVertices[attackMeta.currentFrameIndex]?.let {
                if (ownerFlipX) it.mirror(vec2(0.65f, 0f)) else it
            } ?: return

            body.destroyFixtures()
            val fixture = createLoopFixture(chainShape, "rangeAttackFixture",false)
            body.createFixtures(mutableListOf(fixture))
            attackMeta.hasFixture = true

            val multiplier = if (!ownerFlipX) 1f  else -1f
            body.applyLinearImpulse(Vector2(7f * multiplier, 0f), body.position, true)
        }

        if (keyFrameIx == 2 && animType == AnimationType.THROW) {
            ownerItem?.let { item ->
                val throwableItem = item.itemType as? ItemType.Throwable
                throwableItem?.isThrown = true
            }
        }
    }
}
