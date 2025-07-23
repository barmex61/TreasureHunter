package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.enums.ShaderEffect
import com.libgdx.treasurehunter.enums.ShaderEffectData

enum class FlashType{
    ONCE,CONTINUOUS_WAVE,BLINK
}

data class Flash(
    val shaderEffect: ShaderEffect,
    var flashDuration : Float = 0.2f,
    var flashDurationTimer : Float = flashDuration,
    val flashInterval : Float = 0.2f,
    var flashIntervalTimer : Float = flashInterval,
    var flashTimer : Float = 1f,
    var doFlash : Boolean = true,
    var flashType: FlashType,
    var shaderEffectData : ShaderEffectData = shaderEffect.shaderEffectData.copy(),
    var timer : Float = 1f,
) : Component<Flash> {
    override fun type() = Flash

    companion object : ComponentType<Flash>()
}
