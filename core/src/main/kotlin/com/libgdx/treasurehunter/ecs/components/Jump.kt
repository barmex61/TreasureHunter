package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.enums.SoundAsset
import com.libgdx.treasurehunter.utils.Constants
import com.libgdx.treasurehunter.utils.plus

data class Jump(
    var maxHeight : Float,
    val lowerXY : Vector2,
    val upperXY : Vector2,
    var wantsJump : Boolean = false,
    var doubleJump : Boolean = false,
    val yImpulse : Float = kotlin.math.sqrt(2 * kotlin.math.abs(Constants.GRAVITY.y) * maxHeight),
    val jumpSoundAsset: SoundAsset
) : Component <Jump> {



    override fun type() = Jump

    companion object : ComponentType<Jump>()

}
