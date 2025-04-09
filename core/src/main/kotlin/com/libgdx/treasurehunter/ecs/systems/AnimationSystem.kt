package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.AttackMeta
import com.libgdx.treasurehunter.ecs.components.GdxAnimation
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.TextureAtlasAssets
import com.libgdx.treasurehunter.utils.GameObject
import ktx.app.gdxError
import ktx.log.logger
import kotlin.collections.plusAssign
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
        if (animationComp.gdxAnimation == null) return
        val (gdxAnimation,timer,_,_) = animationComp
        val graphic = entity[Graphic]
        val (sprite) = graphic

        sprite.apply {
            setRegion(gdxAnimation!!.getKeyFrame(timer))
            entity.getOrNull(Move)?.let { moveComp ->
                if (moveComp.flipX != isFlipX){
                    setFlip(true,false)
                }
            }
        }
        if (entity has AttackMeta){
            if (entity[AttackMeta].isFixtureMirrored != sprite.isFlipX){
                sprite.setFlip(true,false)
            }
        }

        val velocityMultiplier = if (entity has Physic) abs(entity[Physic].body.linearVelocity.x / 4f) else 1f
        animationComp.timer += (deltaTime * max(1f,velocityMultiplier))

    }

    fun entityAnimation(entity: Entity, animationType: AnimationType,playMode: PlayMode,frameDuration: Float? = null) {

        val (_,_,_,_,_,gameObject) = entity[Animation]
        val animationAtlasKey = "${gameObject.atlasKey}/${animationType.atlasKey}"

        val gdxAnimation = gdxAnimationCache.getOrPut(animationAtlasKey){
            val regions = gameObjectAtlas.findRegions(animationAtlasKey)
            if (regions.isEmpty){
                gdxError("There are no regions for the animation $animationAtlasKey")
            }
            GdxAnimation(entity[Animation].frameDuration,regions,playMode)
        }.apply {
            this.playMode = playMode
            this.frameDuration = frameDuration ?: entity[Animation].frameDuration
        }
        if (gdxAnimationCache.size > 100){
            log.info { "Animation cache is larger than 100" }
        }
        val animationComp = entity[Animation]
        animationComp.timer = 0f
        animationComp.gdxAnimation = gdxAnimation
        animationComp.playMode = playMode
        animationComp.animationType = animationType

        entity[Graphic].sprite.apply {
            setRegion(gdxAnimation.getKeyFrame(0f))
            entity.getOrNull(Move)?.let { moveComp ->
                if (moveComp.flipX != isFlipX){
                    setFlip(true,false)
                }
            }
        }
    }

    companion object{
        private val log = logger<AnimationSystem>()
    }
}
