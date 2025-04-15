package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.ChainShape
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
import com.libgdx.treasurehunter.utils.Constants.ATTACK_EFFECT_FIXTURES
import com.libgdx.treasurehunter.utils.Constants.ATTACK_FIXTURES
import com.libgdx.treasurehunter.utils.FixtureDefUserData
import com.libgdx.treasurehunter.utils.calculateEffectPosition
import com.libgdx.treasurehunter.utils.copy
import com.libgdx.treasurehunter.utils.createFixtures
import com.libgdx.treasurehunter.utils.destroyFixtures
import ktx.math.vec2
import ktx.math.plus
import com.libgdx.treasurehunter.utils.mirror
import com.libgdx.treasurehunter.utils.offset
import com.libgdx.treasurehunter.utils.width
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
        val ownerBottomCenter = ownerGraphic.bottomCenter
        val ownerFrameIx = ownerAnim.getAttackAnimKeyFrameIx()
        if (attackMeta.attackHandler.gameObjectAtlas == null){
            attackMeta.attackHandler.gameObjectAtlas = assetHelper[TextureAtlasAssets.GAMEOBJECT]
        }
        attackMeta.attackHandler.update(
            attackMetaData = attackMetaData,
            deltaTime = deltaTime,
            entity = entity,
            graphic = graphic,
            body = body,
            ownerFrameIx = ownerFrameIx
        )
        attackMeta.attackHandler.handleAttack(
            entity = entity,
            entityGraphic = graphic,
            owner = owner,
            ownerItem = ownerItem,
            ownerAnim = ownerAnim,
            ownerGraphic = ownerGraphic,
            ownerBottomCenter = ownerBottomCenter,
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

    fun update(attackMetaData: AttackMetaData, deltaTime: Float, entity: Entity, graphic: Graphic, body: Body,ownerFrameIx : Int) {
        attackMetaData.attackDestroyTime -= deltaTime
        if (attackMetaData.attackDestroyTime <= 0f) {
            GameEventDispatcher.fireEvent(GameEvent.RemoveEntityEvent(entity))
            attackMetaData.attackDestroyTime = attackMetaData.baseAttackDestroyTime
        }
        graphic.sprite.setAlpha(if (body.fixtureList.isEmpty) 0f else 1f)

        if (ownerFrameIx == -1 && body.fixtureList.isNotEmpty()) {
            body.destroyFixtures()
        }
    }

    fun handleAttack(
        entity: Entity,
        entityGraphic: Graphic,
        owner: Entity,
        ownerItem : Item?,
        ownerAnim: Animation,
        ownerGraphic: Graphic,
        ownerBottomCenter: Vector2,
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

    protected fun getTextureRegion(modelName: String, attackType: AttackType, keyFrameIndex: Int): TextureRegion {
        val atlasKey = "${modelName}/${attackType.name.lowercase()}"
        return regionsCache.getOrPut(atlasKey) {
            gameObjectAtlas?.findRegions(atlasKey) ?: gdxError("No regions for animation $atlasKey")
        }.get(keyFrameIndex)
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
        ownerBottomCenter: Vector2,
        ownerFlipX: Boolean,
        currentFrameIndex: Int,
        attackMeta: AttackMeta,
        body: Body,
        attackType: AttackType
    ) {
        val keyFrameIndex = ownerAnim.getAttackAnimKeyFrameIx()
        body.setTransform(ownerBottomCenter, body.angle)
        if (keyFrameIndex != -1 && (keyFrameIndex != currentFrameIndex || ownerFlipX != attackMeta.isFixtureMirrored)) {
            body.destroyFixtures()
            val spriteWidth = ownerGraphic.sprite.width
            attackMeta.currentFrameIndex = keyFrameIndex
            attackMeta.isFixtureMirrored = ownerFlipX
            val fixtureDefList = mutableListOf<FixtureDefUserData>()
            val attackFixture = ATTACK_FIXTURES[Pair(attackType,keyFrameIndex)]?.let {
                if (ownerFlipX) it.copy(
                    fixtureDef = it.fixtureDef.copy(shape = (it.fixtureDef.shape as ChainShape).mirror(vec2(spriteWidth/2f,0f)))
                ) else it.copy(fixtureDef = it.fixtureDef.copy(shape = (it.fixtureDef.shape as ChainShape).offset(vec2(-spriteWidth/2f,0f))))
            }
            val effectFixture = createEffectFixture(keyFrameIndex, attackType, ownerFlipX, entityGraphic)
            if (attackFixture != null) fixtureDefList.add(attackFixture)
            if (effectFixture != null) fixtureDefList.add(effectFixture)
            body.createFixtures(fixtureDefList)
            fixtureDefList.clear()
        }


    }

    private fun createEffectFixture(
        keyFrameIndex: Int,
        attackType: AttackType,
        ownerFlipX: Boolean,
        graphic: Graphic
    ): FixtureDefUserData? {
        val offset = if (ownerFlipX) vec2(-0.3f, 0.15f) else vec2(0.3f, 0.15f)
        val effectOffset = if (ownerFlipX) vec2(offset.x - graphic.sprite.width, offset.y) else offset
        val attackEffectFixture = ATTACK_EFFECT_FIXTURES[Pair(attackType,keyFrameIndex)]?.let {
            if (ownerFlipX) it.copy(
                fixtureDef = it.fixtureDef.copy(shape = (it.fixtureDef.shape as ChainShape).mirror(offset))
            ) else it.copy(
                fixtureDef = it.fixtureDef.copy(shape = (it.fixtureDef.shape as ChainShape).offset(offset))
            )
        } ?: return null

        val region = getTextureRegion("attack_effect", attackType, keyFrameIndex)
        graphic.sprite.setRegion(region)
        graphic.effectOffset.set(effectOffset)
        setFlipX(ownerFlipX, graphic.sprite)

        return attackEffectFixture
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
        ownerBottomCenter: Vector2,
        ownerFlipX: Boolean,
        currentFrameIndex: Int,
        attackMeta: AttackMeta,
        body: Body,
        attackType: AttackType
    ) {
        val keyFrameIx = ownerAnim.getAttackAnimKeyFrameIx()
        val animType = ownerAnim.animationData.animationType

        if (keyFrameIx == 1 && !attackMeta.hasFixture) {
            val attackFixture = ATTACK_FIXTURES[Pair(attackType,keyFrameIx)]?.let {
                if (ownerFlipX) it.copy(
                    fixtureDef = it.fixtureDef.copy(shape = (it.fixtureDef.shape as ChainShape).mirror(vec2()))
                ) else it
            } ?: return

            body.destroyFixtures()
            body.createFixtures(mutableListOf(attackFixture))
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
