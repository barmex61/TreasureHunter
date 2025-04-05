package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Life(
    val maxLife : Float ,
    val currentLife : Float  = maxLife,
    var damageTaken : Float = 0f,
) : Component <Life> {

    override fun type() = Life

    companion object : ComponentType<Life>()

}
