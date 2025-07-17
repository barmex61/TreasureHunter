package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.enums.ShaderEffect
import com.libgdx.treasurehunter.enums.ShaderEffectData

data class Flash(
    val shaderEffect: ShaderEffect,
    var flashAmount : Int,
    var flashDuration : Float,
    var flashDurationTimer : Float = flashDuration,
    val flashInterval : Float,
    var flashIntervalTimer : Float = flashInterval,
    var doFlash : Boolean = true,
    val isContinuous : Boolean = false,
    var shaderEffectData : ShaderEffectData? = null,
    var timer : Float = 1f,
) : Component<Flash> {
    override fun type() = Flash

    companion object : ComponentType<Flash>()
}
