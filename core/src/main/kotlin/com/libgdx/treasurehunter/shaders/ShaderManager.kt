package com.libgdx.treasurehunter.shaders

import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.libgdx.treasurehunter.enums.ShaderEffect
import com.libgdx.treasurehunter.utils.ColorSettings

class ShaderManager(val shader: ShaderProgram) {
    var currentShaderEffect : ShaderEffect? = null
    fun applyShaderEffect(effect: ShaderEffect) {
        currentShaderEffect = effect
        shader.bind()
        shader.setUniformf("u_saturation", effect.saturation)
        shader.setUniformf("u_brightness", effect.brightness)
        shader.setUniformf("u_contrast", effect.contrast)
        shader.setUniformf("u_redTint", effect.redTint)
        shader.setUniformf("u_greenTint", effect.greenTint)
        shader.setUniformf("u_blueTint", effect.blueTint)
    }

    fun resetToNormal() {
        applyShaderEffect(ShaderEffect.NORMAL)
    }
}
