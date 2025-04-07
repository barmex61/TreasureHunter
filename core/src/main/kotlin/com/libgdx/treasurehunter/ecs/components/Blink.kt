package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Blink(
    var maxTime : Float,
    val blinkRatio : Float,
    var timer : Float = 0f
) : Component <Blink> {

    override fun type() = Blink

    companion object : ComponentType<Blink>()

}
