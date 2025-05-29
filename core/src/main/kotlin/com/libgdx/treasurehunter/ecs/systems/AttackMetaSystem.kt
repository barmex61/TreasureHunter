package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.Shape
import com.badlogic.gdx.utils.Array
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.AttackMeta
import com.libgdx.treasurehunter.ecs.components.AttackMetaData
import com.libgdx.treasurehunter.ecs.components.AttackType
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Item
import com.libgdx.treasurehunter.ecs.components.ItemType
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.components.ThrowState
import com.libgdx.treasurehunter.ecs.systems.AnimationSystem.Companion.setFlipX
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.TextureAtlasAssets
import com.libgdx.treasurehunter.utils.Constants.ATTACK_EFFECT_FIXTURES
import com.libgdx.treasurehunter.utils.Constants.ATTACK_FIXTURES
import com.libgdx.treasurehunter.utils.FixtureDefUserData
import com.libgdx.treasurehunter.utils.copy
import com.libgdx.treasurehunter.utils.createFixtures
import com.libgdx.treasurehunter.utils.destroyFixtures
import ktx.math.vec2
import ktx.math.times
import com.libgdx.treasurehunter.utils.mirror
import com.libgdx.treasurehunter.utils.offset
import ktx.app.gdxError
import ktx.collections.isNotEmpty
import kotlin.math.abs


class AttackMetaSystem(
    private val assetHelper: AssetHelper = inject()
) : IteratingSystem(
    family = family { all(AttackMeta, Physic) }
) {

    override fun onTickEntity(entity: Entity) {
        val attackMeta = entity[AttackMeta]
        val (owner, currentFrameIndex,createFrameIndex, _, collideWithWall,  attackMetaData,attackHandler) = attackMeta
        val (body, _) = entity[Physic]
        val graphic = entity[Graphic]
        val ownerFlipX = owner.getOrNull(Move)?.flipX ?: owner.getOrNull(Graphic)?.initialFlipX ?: false
        val ownerAnim = owner[Animation]
        val ownerGraphic = owner[Graphic]
        val ownerItem = owner.getOrNull(Item)
        val ownerBottomCenter = ownerGraphic.bottomCenter
        val ownerFrameIx = ownerAnim.getAttackAnimKeyFrameIx(attackMetaData.attackType)
        if (attackHandler == null){
            attackMeta.setAttackHandler(textureAtlas = assetHelper[TextureAtlasAssets.GAMEOBJECT],world)
        }
        attackHandler?:return
        attackHandler.update(
            attackMetaData = attackMetaData,
            deltaTime = deltaTime,
            entity = entity,
            graphic = graphic,
            body = body,
            ownerFrameIx = ownerFrameIx,
        )
        attackHandler.handleAttack(
            entity = entity,
            entityGraphic = graphic,
            owner = owner,
            ownerItem = ownerItem,
            ownerAnim = ownerAnim,
            ownerGraphic = ownerGraphic,
            ownerBottomCenter = ownerBottomCenter,
            ownerFlipX = ownerFlipX,
            ownerFrameIx = ownerFrameIx,
            currentFrameIndex = currentFrameIndex,
            attackMeta = attackMeta,
            body = body,
            attackType = attackMetaData.attackType
        )
        if (collideWithWall && attackHandler is RangeAttackHandler){
            attackHandler.collideWithWall(body)
        }
    }
}

sealed class AttackHandler(
    val world: World
) {

     abstract val isFixtureInitialized : Boolean

    open fun update(attackMetaData: AttackMetaData, deltaTime: Float, entity: Entity, graphic: Graphic, body: Body,ownerFrameIx : Int) {
        attackMetaData.attackDestroyTime -= deltaTime
        if (attackMetaData.attackDestroyTime <= 0f || (isFixtureInitialized && body.fixtureList.isEmpty)) {
            with(world) {
                entity.configure {
                    it += EntityTag.REMOVE
                }
            }
            attackMetaData.attackDestroyTime = attackMetaData.baseAttackDestroyTime
        }
    }

    abstract fun handleAttack(
        entity: Entity,
        entityGraphic: Graphic,
        owner: Entity,
        ownerItem : Item?,
        ownerAnim: Animation,
        ownerGraphic: Graphic,
        ownerBottomCenter: Vector2,
        ownerFlipX: Boolean,
        ownerFrameIx: Int,
        currentFrameIndex: Int,
        attackMeta: AttackMeta,
        body: Body,
        attackType: AttackType
    )
}


class MeleeAttackHandler(
    private val gameObjectAtlas: TextureAtlas,
    world: World,
) : AttackHandler(world) {
    override var isFixtureInitialized: Boolean = false

    private val regionsCache = mutableMapOf<String, Array<TextureAtlas.AtlasRegion>>()

    private fun getTextureRegion(modelName: String, attackType: AttackType, keyFrameIndex: Int): TextureRegion {
        val atlasKey = "${modelName}/${attackType.name.lowercase()}"
        return regionsCache.getOrPut(atlasKey) {
            gameObjectAtlas.findRegions(atlasKey) ?: gdxError("No regions for animation $atlasKey")
        }.get(keyFrameIndex)
    }

    override fun update(
        attackMetaData: AttackMetaData,
        deltaTime: Float,
        entity: Entity,
        graphic: Graphic,
        body: Body,
        ownerFrameIx: Int
    ) {
        super.update(attackMetaData, deltaTime, entity, graphic, body, ownerFrameIx)
        if (ownerFrameIx == -1 && body.fixtureList.isNotEmpty()) {
            body.destroyFixtures()
        }
        graphic.sprite.setAlpha(if (body.fixtureList.none { it.userData == "attackEffectFixture" }) 0f else 1f)
    }

    override fun handleAttack(
        entity: Entity,
        entityGraphic: Graphic,
        owner: Entity,
        ownerItem: Item?,
        ownerAnim: Animation,
        ownerGraphic: Graphic,
        ownerBottomCenter: Vector2,
        ownerFlipX: Boolean,
        ownerFrameIx : Int,
        currentFrameIndex: Int,
        attackMeta: AttackMeta,
        body: Body,
        attackType: AttackType
    ) {
        body.setTransform(ownerBottomCenter, body.angle)
        if (ownerFrameIx != -1 && (ownerFrameIx != currentFrameIndex || ownerFlipX != attackMeta.isFixtureMirrored)) {
            body.destroyFixtures()
            val spriteWidth = ownerGraphic.sprite.width
            attackMeta.currentFrameIndex = ownerFrameIx
            attackMeta.isFixtureMirrored = ownerFlipX
            val fixtureDefList = mutableListOf<FixtureDefUserData>()
            val attackFixture = ATTACK_FIXTURES[Pair(attackType,ownerFrameIx)]?.let {fixDefList ->
                fixDefList.map{
                    if (ownerFlipX) it.copy(
                        fixtureDef = it.fixtureDef.copy(shape = (it.fixtureDef.shape as ChainShape).mirror(vec2(spriteWidth/2f,0f)))
                    ) else it.copy(fixtureDef = it.fixtureDef.copy(shape = (it.fixtureDef.shape as ChainShape).offset(vec2(-spriteWidth/2f,0f))))
                }
            }
            val effectFixture = createEffectFixture(ownerFrameIx, attackType, ownerFlipX, entityGraphic)
            if (attackFixture != null) fixtureDefList.addAll(attackFixture)
            if (effectFixture != null) fixtureDefList.addAll(effectFixture)
            fixtureDefList.forEach {
                it.fixtureDef.isSensor = true
            }
            body.createFixtures(fixtureDefList)
            isFixtureInitialized = true
            fixtureDefList.clear()
        }
    }

    private fun createEffectFixture(
        keyFrameIndex: Int,
        attackType: AttackType,
        ownerFlipX: Boolean,
        graphic: Graphic
    ): List<FixtureDefUserData>? {
        val offset = if (ownerFlipX) attackType.attackOffset.cpy() else vec2(attackType.attackOffset.x * -1f, 0.15f)
        val effectOffset = if (ownerFlipX) vec2(offset.x - graphic.sprite.width, offset.y) else offset
        val attackEffectFixture = ATTACK_EFFECT_FIXTURES[Pair(attackType,keyFrameIndex)]?.let {fixDefList ->
            fixDefList.map {
                if (ownerFlipX) it.copy(
                    fixtureDef = it.fixtureDef.copy(shape = (it.fixtureDef.shape as ChainShape).mirror(offset))
                ) else it.copy(
                    fixtureDef = it.fixtureDef.copy(shape = (it.fixtureDef.shape as ChainShape).offset(offset))
                )
            }

        } ?: return null

        val region = getTextureRegion("attack_effect", attackType, keyFrameIndex)
        graphic.sprite.setRegion(region)
        graphic.offset.set(effectOffset)
        setFlipX(ownerFlipX, graphic.sprite)
        return attackEffectFixture
    }

}

class RangeAttackHandler(
    world: World
) : AttackHandler(world) {

    override var isFixtureInitialized: Boolean = false

    override fun handleAttack(
        entity: Entity,
        entityGraphic: Graphic,
        owner: Entity,
        ownerItem: Item?,
        ownerAnim: Animation,
        ownerGraphic: Graphic,
        ownerBottomCenter: Vector2,
        ownerFlipX: Boolean,
        ownerFrameIx: Int,
        currentFrameIndex: Int,
        attackMeta: AttackMeta,
        body: Body,
        attackType: AttackType,
    ) {
        val animType = ownerAnim.animationData.animationType

        if (ownerFrameIx == attackMeta.createFrameIndex && !isFixtureInitialized) {
            body.destroyFixtures()
            val attackOffset = if (ownerFlipX) attackType.attackOffset.cpy() else vec2(attackType.attackOffset.x * -1f, attackType.attackOffset.y)
            val attackFixture = ATTACK_FIXTURES[Pair(attackType,0)]?.let {fixDefList ->
                fixDefList.map {
                    if (ownerFlipX) it.copy(
                        fixtureDef = it.fixtureDef.copy(shape = (it.fixtureDef.shape as ChainShape).mirror(attackOffset))
                    ) else it.copy(
                        fixtureDef = it.fixtureDef.copy(shape = (it.fixtureDef.shape as ChainShape).offset(attackOffset))
                    )
                }
            } ?: return
            body.createFixtures(attackFixture)
            if (ownerFlipX) entityGraphic.offset.set(-entityGraphic.sprite.width + attackOffset.x,attackOffset.y)
            entityGraphic.sprite.setAlpha(1f)
            if (entityGraphic.initialFlipX != ownerFlipX){
                entityGraphic.sprite.setFlip(ownerFlipX,false)
            }
            isFixtureInitialized = true
            val multiplier = if (!ownerFlipX) 1f  else -1f
            body.applyLinearImpulse(Vector2(7f * multiplier, 0f), body.position, true)
        }

        if (ownerFrameIx == 2 && animType == AnimationType.THROW) {
            ownerItem?.let { item ->
                val throwableItem = item.itemType as? ItemType.Throwable
                throwableItem?.throwState = ThrowState.THROWED
            }
        }
    }
    fun collideWithWall(
        body: Body
    ){
        if (body.linearVelocity != Vector2.Zero){
            body.linearVelocity = vec2(0f,0f)
        }
    }
}
