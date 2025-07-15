package com.libgdx.treasurehunter.enums

import com.libgdx.treasurehunter.utils.ColorSettings

enum class ShaderEffect(
    val saturation: Float,
    val brightness: Float,
    val contrast: Float,
    val redTint: Float = 1f,
    val greenTint: Float = 1f,
    val blueTint: Float = 1f
) {
    EVENING(
        saturation = 0.7f,
        brightness = 0.8f,
        contrast = 1.1f
    ),
    NIGHT(
        saturation = 0.6f,
        brightness = 0.6f,
        contrast = 1.2f
    ),
    DAY(
        saturation = 1.0f,
        brightness = 1.0f,
        contrast = 1.0f
    ),
    CAVE(
        saturation = 0.5f,
        brightness = 0.4f,
        contrast = 1.4f
    ),
    UNDERWATER(
        saturation = 1.2f,
        brightness = 0.8f,
        contrast = 1.1f
    ),
    FOREST(
        saturation = 1.3f,
        brightness = 0.9f,
        contrast = 1.1f
    ),
    DESERT(
        saturation = 1.4f,
        brightness = 1.2f,
        contrast = 1.1f
    ),
    SNOW(
        saturation = 0.7f,
        brightness = 1.3f,
        contrast = 1.2f
    ),
    HIT_EFFECT(
        saturation = 1.5f,
        brightness = 1.2f,
        contrast = 1.3f,
        redTint = 1.5f,
        greenTint = 0.5f,
        blueTint = 0.5f
    ),
    POISON_EFFECT(
        saturation = 1.3f,
        brightness = 0.9f,
        contrast = 1.2f,
        redTint = 0.5f,
        greenTint = 1.5f,
        blueTint = 0.5f
    ),
    FREEZE_EFFECT(
        saturation = 0.8f,
        brightness = 1.1f,
        contrast = 1.2f,
        redTint = 0.7f,
        greenTint = 0.7f,
        blueTint = 1.5f
    ),
    INVISIBLE_EFFECT(
        saturation = 0.5f,
        brightness = 0.7f,
        contrast = 1.1f,
        redTint = 1f,
        greenTint = 1f,
        blueTint = 1f
    ),
    BURN_EFFECT(
        saturation = 1.4f,
        brightness = 1.3f,
        contrast = 1.4f,
        redTint = 1.8f,
        greenTint = 0.8f,
        blueTint = 0.8f
    ),
    HEAL_EFFECT(
        saturation = 1.2f,
        brightness = 1.1f,
        contrast = 1.1f,
        redTint = 0.8f,
        greenTint = 1.8f,
        blueTint = 0.8f
    ),
    NORMAL(
        saturation = 1.0f,
        brightness = 1.0f,
        contrast = 1.0f
    );

    companion object {
        fun fromColorSettings(settings: ColorSettings): ShaderEffect {
            return when (settings) {
                ColorSettings.EVENING -> EVENING
                ColorSettings.NIGHT -> NIGHT
                ColorSettings.DAY -> DAY
                ColorSettings.CAVE -> CAVE
                ColorSettings.UNDERWATER -> UNDERWATER
                ColorSettings.FOREST -> FOREST
                ColorSettings.DESERT -> DESERT
                ColorSettings.SNOW -> SNOW
            }
        }
    }
}
