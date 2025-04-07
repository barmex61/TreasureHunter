package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Life(
    val maxLife : Int,
    var currentLife : Int  = maxLife,
    var damageTaken : Int = 0,
    var isDead : Boolean = false
) : Component <Life> {

    override fun type() = Life

    companion object : ComponentType<Life>()

}
