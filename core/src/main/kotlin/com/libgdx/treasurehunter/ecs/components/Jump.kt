package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.utils.Constants

data class Jump(
    var maxHeight : Float,
    var wantsJump : Boolean = false,
    var canJump : Boolean = true,
    var doubleJump : Boolean = false,
    val yImpulse : Float = kotlin.math.sqrt(2 * kotlin.math.abs(Constants.GRAVITY.y) * maxHeight)
) : Component <Jump> {


    override fun type() = Jump

    companion object : ComponentType<Jump>()

}
