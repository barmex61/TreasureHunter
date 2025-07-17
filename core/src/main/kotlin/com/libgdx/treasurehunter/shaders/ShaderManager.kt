package com.libgdx.treasurehunter.shaders

import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.libgdx.treasurehunter.enums.ShaderEffectData

class ShaderManager(val shader: ShaderProgram,val onShaderEffectChange : (ShaderProgram) -> Unit) {
    fun applyShaderEffect(shaderEffectData: ShaderEffectData) {
        shader.bind()
        shader.setUniformf("u_saturation", shaderEffectData.saturation)
        shader.setUniformf("u_brightness", shaderEffectData.brightness)
        shader.setUniformf("u_contrast", shaderEffectData.contrast)
        shader.setUniformf("u_redTint", shaderEffectData.redTint)
        shader.setUniformf("u_greenTint", shaderEffectData.greenTint)
        shader.setUniformf("u_blueTint", shaderEffectData.blueTint)
        onShaderEffectChange(shader)
    }


}
