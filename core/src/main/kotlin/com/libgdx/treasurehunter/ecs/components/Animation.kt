package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.utils.GameObject


typealias GdxAnimation = com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>

enum class AnimationType(val isAttackAnimation : Boolean = false){
    NONE,AIR_ATTACK_1(true), AIR_ATTACK_2(true), ATTACK_1(true), ATTACK_2(true), ATTACK_3(true), FALL, GROUND, HIT, IDLE, JUMP, RUN, THROW(true), DEAD_GROUND, DEAD_HIT,IN,OUT,SPINNING,EMBEDDED;
    val atlasKey : String = this.name.lowercase()
}

data class Animation(
    var gdxAnimation : GdxAnimation? = null,
    var timer : Float = 0f,
    var frameDuration : Float,
    var playMode: PlayMode = PlayMode.LOOP,
    var animationType: AnimationType = AnimationType.IDLE,
    var gameObject: GameObject
) : Component<Animation> {
    fun getAttackAnimKeyFrameIx() : Int{
        return if (animationType.isAttackAnimation){
            gdxAnimation?.getKeyFrameIndex(timer) ?: 0
        }else{
            -1
        }
    }
    fun setNewGameObject(newGameObject: GameObject){
        gameObject = newGameObject
        timer = 0f
    }
    override fun type() : ComponentType<Animation> = Animation
    companion object : ComponentType<Animation>()
}
