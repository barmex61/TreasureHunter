package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.utils.GameObject


typealias GdxAnimation = com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>

enum class AnimationType{
    NONE,ON,OPEN,CLOSE,LOCK,DESTROYED,UNLOCK,REUNLOCK,SPLASH_1,WIND,SPLASH_2,REFLEXES_1,REFLEXES_2,ATTACK,FIERCE_TOOTH_ATTACK,CRABBY_ATTACK,PINK_STAR_ATTACK,AIR_ATTACK_1, AIR_ATTACK_2, ATTACK_1, ATTACK_2, ATTACK_3, FALL, GROUND, HIT, IDLE, JUMP, RUN, THROW, DEAD_GROUND, DEAD_HIT,IN,OUT,SPINNING,EMBEDDED,
    POTION,MAP,SKULL,COIN,DIAMOND,INTERROGATION,EXCLAMATION,DEAD;
    val atlasKey : String = this.name.lowercase()
}

data class AnimationData(
    var gdxAnimation: GdxAnimation? = null,
    var timer: Float = 0f,
    var playMode: PlayMode = PlayMode.LOOP,
    var animationType: AnimationType ,
    var frameDuration: Float = 0.1f
)

data class Animation(
    var modelName: String,
    val animationData: AnimationData,
    var flipInitialized : Boolean = false
)  : Component<Animation> {
    fun getAttackAnimKeyFrameIx(attackType: AttackType) : Int{
        return if (animationData.animationType == attackType.attackAnimType){
            animationData.gdxAnimation?.getKeyFrameIndex(animationData.timer) ?: 0
        }else{
            -1
        }
    }
    fun getAnimKeyFrameIx() : Int{
        return animationData.gdxAnimation?.getKeyFrameIndex(animationData.timer) ?: 0
    }
    fun isAnimationDone() : Boolean{
        if (animationData.playMode == PlayMode.LOOP) return false
        return animationData.gdxAnimation?.isAnimationFinished(animationData.timer) == true
    }
    fun setNewModel(modelName : String){
        this.modelName = modelName
        animationData.timer = 0f
    }
    override fun type() : ComponentType<Animation> = Animation
    companion object : ComponentType<Animation>()
}
