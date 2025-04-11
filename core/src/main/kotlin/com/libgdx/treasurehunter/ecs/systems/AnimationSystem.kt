package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.Sprite
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationData
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.AttackMeta
import com.libgdx.treasurehunter.ecs.components.GdxAnimation
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Particle
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.TextureAtlasAssets
import com.libgdx.treasurehunter.utils.GameObject
import ktx.app.gdxError
import ktx.log.logger
import kotlin.math.abs
import kotlin.math.max

class AnimationSystem (
    assetHelper: AssetHelper = inject()
): IteratingSystem(
    family = family { all(Animation, Graphic)}
) {
    private val gameObjectAtlas = assetHelper[TextureAtlasAssets.GAMEOBJECT]
    private val gdxAnimationCache = mutableMapOf<String, GdxAnimation>()

    override fun onTickEntity(entity: Entity) {
        val animationComp = entity[Animation]
        val (gameObject, mainAnimationData) = animationComp
        val graphic = entity[Graphic]
        val (sprite) = graphic
        val (mainAnimation,timer,mainPlayMode,mainAnimationType,mainFrameDuration) = mainAnimationData
        val velocityMultiplier = if (entity has Physic) abs(entity[Physic].body.linearVelocity.x / 4f) else 1f
        mainAnimationData.timer += (deltaTime * max(1f,velocityMultiplier))
        if (mainAnimation == null){
            setAnimation(entity,mainAnimationType,mainPlayMode,mainFrameDuration)
            if (entity has Particle){
                setFlipX(entity[Particle].owner.getOrNull(Move),sprite)
            }
        }
        updateSprite(sprite,entity,mainAnimationData)

        if (entity has AttackMeta){
            if (entity[AttackMeta].isFixtureMirrored != sprite.isFlipX){
                sprite.setFlip(true,false)
            }
        }

        if (gdxAnimationCache.size > 100) {
            gdxAnimationCache.clear()
            log.info { "Animation cache cleared" }
        }

    }

    private fun updateSprite(sprite: Sprite, entity: Entity, animData: AnimationData,alpha : Float? = null) {
        val gdxAnim = animData.gdxAnimation ?: return
        val flipX = sprite.isFlipX
        sprite.apply {
            setRegion(gdxAnim.getKeyFrame(animData.timer))
            setAlpha(alpha?:sprite.color.a)
            setFlip(flipX,false)
        }
        setFlipX(entity.getOrNull(Move),sprite)
    }

    private fun setFlipX (moveComp : Move?, sprite: Sprite){

        moveComp?.let { moveComp ->
            if (moveComp.flipX != sprite.isFlipX) {
                sprite.setFlip(moveComp.flipX, false)
            }
        }
    }

    private fun createOrGetAnimation(
        gameObject: GameObject,
        animationType: AnimationType,
        frameDuration: Float,
        playMode: PlayMode
    ): GdxAnimation {
        val atlasKey = "${gameObject.atlasKey}/${animationType.atlasKey}"
        return gdxAnimationCache.getOrPut(atlasKey) {
            val regions = gameObjectAtlas.findRegions(atlasKey) ?:
            gdxError("No regions for animation $atlasKey")
            GdxAnimation(frameDuration, regions, playMode)
        }.apply {
            this.playMode = playMode
            this.frameDuration = frameDuration
        }
    }

    fun setAnimation(
        entity: Entity,
        animationType: AnimationType,
        playMode: PlayMode = PlayMode.LOOP,
        frameDuration: Float? = null
    ) {
        val animComp = entity[Animation]
        val animData = animComp.animationData
        updateAnimationData(
            animData.apply { timer = 0f },
            animComp.gameObject,
            animationType,
            playMode,
            frameDuration
        )
    }

    private fun updateAnimationData(
        animData: AnimationData,
        gameObject: GameObject,
        newAnimType: AnimationType,
        newPlayMode: PlayMode,
        newFrameDuration: Float? = null
    ) {
        val finalFrameDuration = newFrameDuration ?: animData.frameDuration
        animData.apply {
            gdxAnimation = createOrGetAnimation(gameObject, newAnimType, finalFrameDuration, newPlayMode)
            timer = 0f
            playMode = newPlayMode
            animationType = newAnimType
            frameDuration = finalFrameDuration
        }
    }

    companion object{
        private val log = logger<AnimationSystem>()
    }
}
