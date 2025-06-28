package com.libgdx.treasurehunter.utils

import com.badlogic.gdx.utils.ObjectMap

fun ObjectMap<String,String>.toGameProperties() : GameProperties {
    return GameProperties(
        debugPhysic = getOrDefault("debugPhysic", false),
        enableProfiling = this["enableProfiling"]?.toBoolean() == true,
        musicVolume = this["musicVolume"]?.toFloat() ?: 1f,
        soundVolume = this["soundVolume"]?.toFloat() ?: 1f
    )
}

private inline fun <reified T> ObjectMap<String, String>.getOrDefault(key: String, defaultValue: T): T {
    val strValue = this[key] ?: return defaultValue
    return when(T::class){
        Boolean::class -> strValue.toBoolean() as T
        Float::class -> strValue.toFloat() as T
        Int::class -> strValue.toInt() as T
        else -> strValue as T
    }
}

data class GameProperties(
    val debugPhysic : Boolean,
    val enableProfiling : Boolean,
    val musicVolume: Float,
    val soundVolume : Float
)
