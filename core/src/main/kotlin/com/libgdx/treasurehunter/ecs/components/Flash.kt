package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.graphics.Color
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Flash(
    val color : Color,
    var flashAmount : Int,
    var flashDuration : Float,
    var flashDurationTimer : Float = flashDuration,
    val flashInterval : Float,
    var flashIntervalTimer : Float = flashInterval,
    var doFlash : Boolean = true
) : Component<Flash> {
    override fun type() = Flash

    companion object : ComponentType<Flash>()
}
