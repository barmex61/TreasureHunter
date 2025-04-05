package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.utils.GameObject


typealias GdxAnimation = com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>

enum class AnimationType{
    NONE,AIR_ATTACK_1, AIR_ATTACK_2, ATTACK_1, ATTACK_2, ATTACK_3, FALL, GROUND, HIT, IDLE, JUMP, RUN, THROW;
    val atlasKey : String = this.name.lowercase()
}

data class Animation(
    var gdxAnimation : GdxAnimation? = null,
    var timer : Float = 0f,
    var frameDuration : Float,
    var playMode: PlayMode = PlayMode.LOOP,
    var animationType: AnimationType = AnimationType.IDLE,
    val gameObject: GameObject
) : Component<Animation> {

    override fun type() : ComponentType<Animation> = Animation
    companion object : ComponentType<Animation>()
}
