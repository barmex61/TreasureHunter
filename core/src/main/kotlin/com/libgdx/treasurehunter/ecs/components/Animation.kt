package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.utils.GameObject


typealias GdxAnimation = com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>

enum class AnimationType(val isAttackAnimation : Boolean = false){
    NONE,FIERCE_TOOTH_ATTACK(true),CRABBY_ATTACK(true),PINK_STAR_ATTACK(true),AIR_ATTACK_1(true), AIR_ATTACK_2(true), ATTACK_1(true), ATTACK_2(true), ATTACK_3(true), FALL, GROUND, HIT, IDLE, JUMP, RUN, THROW(true), DEAD_GROUND, DEAD_HIT,IN,OUT,SPINNING,EMBEDDED;
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
