package com.libgdx.treasurehunter.enums

data class ShaderEffectData(
    val saturation: Float,
    val brightness: Float,
    val contrast: Float,
    var redTint: Float = 1f,
    var greenTint: Float = 1f,
    var blueTint: Float = 1f,
    val shaderInterpolateChannels : Set<InterpolateChannels> = setOf(
        InterpolateChannels.GREEN,
        InterpolateChannels.BLUE,
        InterpolateChannels.RED
    ),
    val amplitude : Float = 0.75f,
    val frequency : Float = 6f
)

enum class InterpolateChannels{
    GREEN,BLUE,RED
}
enum class ShaderEffect(
    val shaderEffectData: ShaderEffectData
) {
    EVENING(
        ShaderEffectData(
            saturation = 0.7f,
            brightness = 0.8f,
            contrast = 1.1f
        )
    ),
    NIGHT(
        ShaderEffectData(
            saturation = 0.6f,
            brightness = 0.6f,
            contrast = 1.2f
        )
    ),
    DAY(
        ShaderEffectData(
            saturation = 1.0f,
            brightness = 1.0f,
            contrast = 1.0f
        )

    ),
    CAVE(
        ShaderEffectData(
            saturation = 0.5f,
            brightness = 0.4f,
            contrast = 1.4f
        )

    ),
    UNDERWATER(
        ShaderEffectData(
            saturation = 1.2f,
            brightness = 0.8f,
            contrast = 1.1f
        )

    ),
    FOREST(
        ShaderEffectData(
            saturation = 1.3f,
            brightness = 0.9f,
            contrast = 1.1f
        )

    ),
    DESERT(
        ShaderEffectData(
            saturation = 1.4f,
            brightness = 1.2f,
            contrast = 1.1f
        )

    ),
    SNOW(
        ShaderEffectData(
            saturation = 0.7f,
            brightness = 1.3f,
            contrast = 1.2f
        )

    ),
    HIT_EFFECT(
        ShaderEffectData(
            saturation = 1.5f,
            brightness = 1.2f,
            contrast = 1.3f,
            redTint = 1.5f,
            greenTint = 0.5f,
            blueTint = 0.5f
        )

    ),
    POISON_EFFECT(
        ShaderEffectData(
            saturation = 1.3f,
            brightness = 0.9f,
            contrast = 1.2f,
            redTint = 0.5f,
            greenTint = 1.5f,
            blueTint = 0.5f
        )

    ),
    FREEZE_EFFECT(
        ShaderEffectData(
            saturation = 0.8f,
            brightness = 1.1f,
            contrast = 1.2f,
            redTint = 0.7f,
            greenTint = 0.7f,
            blueTint = 1.5f
        )

    ),
    INVISIBLE_EFFECT(
        ShaderEffectData(
            saturation = 0.5f,
            brightness = 0.7f,
            contrast = 1.1f,
            redTint = 1f,
            greenTint = 1f,
            blueTint = 1f
        )

    ),
    BURN_EFFECT(
        ShaderEffectData(
            saturation = 1.4f,
            brightness = 1.3f,
            contrast = 1.4f,
            redTint = 1.8f,
            greenTint = 0.8f,
            blueTint = 0.8f
        )

    ),
    HEAL_EFFECT(
        ShaderEffectData(
            saturation = 1.2f,
            brightness = 1.1f,
            contrast = 1.1f,
            redTint = 0.8f,
            greenTint = 1.8f,
            blueTint = 0.8f
        )

    ),
    ITEM_APPEAR(
        ShaderEffectData(
            saturation = 1.2f,
            brightness = 1.5f,
            contrast = 1.3f,
            redTint = 0.4f,
            greenTint = 1.7f,
            blueTint = 0.4f
        )
    ),
    NORMAL(
        ShaderEffectData(
            saturation = 1.0f,
            brightness = 1.0f,
            contrast = 1.0f
        )

    ),
    GREEN_EFFECT(
        ShaderEffectData(
            saturation = 0.9f,
            brightness = 1.1f,
            contrast = 1.2f,
            redTint = 0.6f,
            greenTint = 1.4f,
            blueTint = 0.6f
        ),
    ),
    BLUE_EFFECT(
        ShaderEffectData(
            saturation = 1.8f,
            brightness = 1.1f,
            contrast = 1.5f,
            redTint = 0.5f,
            greenTint = 0.5f,
            blueTint = 1.6f
        ),
    ),
    RED_EFFECT(
        ShaderEffectData(
            saturation = 1.1f,
            brightness = 1f,
            contrast = 1.4f,
            redTint = 1.7f,
            greenTint = 0.3f,
            blueTint = 0.3f
        ),
    ),
    BROWN_EFFECT(
        ShaderEffectData(
            saturation = 0.3f,
            brightness = 1f,
            contrast = 0.8f,
            redTint = 0.8f,
            greenTint = 0.5f,
            blueTint = 0.4f
        ),
    ),
    YELLOW_EFFECT(
        ShaderEffectData(
            saturation = 0.85f,
            brightness = 1f,
            contrast = 0.85f,
            redTint = 0.95f,
            greenTint = 0.95f,
            blueTint = 0f
        ),
    ),
    GRAY_EFFECT(
        ShaderEffectData(
            saturation = 0.5f,
            brightness = 1f,
            contrast = 0.8f,
            redTint = 0.7f,
            greenTint = 0.7f,
            blueTint = 0.7f
        ),
    ),
    HELMET_EFFECT(
        ShaderEffectData(
            saturation = 1.2f,
            brightness = 1.1f,
            contrast = 1.3f,
            redTint = 1.2f,
            greenTint = 1.2f,
            blueTint = 1.2f ,
        ),
    ),
    BOOTS_EFFECT(
        ShaderEffectData(
            saturation = 1.4f,
            brightness = 1.3f,
            contrast = 1.1f,
            redTint = 1.5f,
            greenTint = 1.3f,
            blueTint = 0.7f
        ),

    ),
    ARMOR_EFFECT(
        ShaderEffectData(
            saturation = 1.3f,
            brightness = 1.2f,
            contrast = 1.2f,
            redTint = 0.75f,
            greenTint = 0.8f,
            blueTint = 0.9f,
            amplitude = 0.3f
        ),
    );
}
