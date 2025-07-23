package com.libgdx.treasurehunter.enums

import com.badlogic.gdx.math.Vector2
import com.libgdx.treasurehunter.ecs.components.AnimationType
import ktx.math.vec2

enum class EffectType(
    val offset: Vector2 = vec2(),
    val animationType: AnimationType,
    val shaderEffect: ShaderEffect,
) {
    DEAD_MARK(Vector2(0f, 0.3f), AnimationType.DEAD, ShaderEffect.HIT_EFFECT),
    INTERROGATION_MARK(Vector2(0f, 0.3f), AnimationType.INTERROGATION, ShaderEffect.FREEZE_EFFECT),
    EXCLAMATION_MARK(Vector2(0f, 0.3f), AnimationType.EXCLAMATION, ShaderEffect.FREEZE_EFFECT),
    RED_POTION(animationType = AnimationType.POTION, shaderEffect = ShaderEffect.RED_EFFECT),
    BLUE_POTION(animationType = AnimationType.POTION, shaderEffect = ShaderEffect.BLUE_EFFECT),
    MAP(animationType = AnimationType.MAP, shaderEffect = ShaderEffect.BROWN_EFFECT),
    BLUE_DIAMOND(animationType = AnimationType.DIAMOND, shaderEffect = ShaderEffect.BLUE_EFFECT),
    GREEN_DIAMOND(animationType = AnimationType.DIAMOND, shaderEffect = ShaderEffect.GREEN_EFFECT),
    RED_DIAMOND(animationType = AnimationType.DIAMOND, shaderEffect = ShaderEffect.RED_EFFECT),
    SILVER_COIN(animationType = AnimationType.COIN, shaderEffect = ShaderEffect.GRAY_EFFECT),
    GOLD_COIN(animationType = AnimationType.COIN, shaderEffect = ShaderEffect.YELLOW_EFFECT),
    SKULL(animationType = AnimationType.SKULL, shaderEffect = ShaderEffect.RED_EFFECT)

}
