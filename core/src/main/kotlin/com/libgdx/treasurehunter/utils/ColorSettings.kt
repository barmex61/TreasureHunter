package com.libgdx.treasurehunter.utils

import com.badlogic.gdx.graphics.Color
import com.libgdx.treasurehunter.enums.ShaderEffect

enum class ColorSettings(
    val backgroundFarColor: Color,
    val backgroundMidColor: Color,
    val backgroundCloseColor: Color,
    val groundColor: Color,
    val entityColor: Color,
    val shaderEffect: ShaderEffect
) {
    EVENING(
        backgroundFarColor = Color(42f/255f, 42f/255f, 42f/255f, 0.7f),
        backgroundMidColor = Color(58f/255f, 58f/255f, 58f/255f, 0.8f),
        backgroundCloseColor = Color(74f/255f, 74f/255f, 74f/255f, 0.9f),
        groundColor = Color(85f/255f, 85f/255f, 85f/255f, 1f),
        entityColor = Color(150f/255f, 150f/255f, 150f/255f, 1f),
        shaderEffect = ShaderEffect.EVENING
    ),

    NIGHT(
        backgroundFarColor = Color(20f/255f, 20f/255f, 30f/255f, 0.8f),
        backgroundMidColor = Color(30f/255f, 30f/255f, 40f/255f, 0.9f),
        backgroundCloseColor = Color(40f/255f, 40f/255f, 50f/255f, 1f),
        groundColor = Color(45f/255f, 45f/255f, 55f/255f, 1f),
        entityColor = Color(80f/255f, 80f/255f, 100f/255f, 1f),
        shaderEffect = ShaderEffect.NIGHT
    ),

    DAY(
        backgroundFarColor = Color.WHITE,
        backgroundMidColor = Color.WHITE,
        backgroundCloseColor = Color.WHITE,
        groundColor = Color(0.95f, 0.95f, 0.95f, 1f),
        entityColor = Color(1f, 1f, 1f, 1f),
        shaderEffect = ShaderEffect.DAY
    ),

    CAVE(
        backgroundFarColor = Color(10f/255f, 10f/255f, 10f/255f, 0.9f),
        backgroundMidColor = Color(20f/255f, 20f/255f, 20f/255f, 1f),
        backgroundCloseColor = Color(30f/255f, 30f/255f, 30f/255f, 1f),
        groundColor = Color(35f/255f, 35f/255f, 35f/255f, 1f),
        entityColor = Color(60f/255f, 60f/255f, 60f/255f, 1f),
        shaderEffect = ShaderEffect.CAVE
    ),

    UNDERWATER(
        backgroundFarColor = Color(20f/255f, 40f/255f, 60f/255f, 0.7f),
        backgroundMidColor = Color(30f/255f, 50f/255f, 70f/255f, 0.8f),
        backgroundCloseColor = Color(40f/255f, 60f/255f, 80f/255f, 0.9f),
        groundColor = Color(45f/255f, 65f/255f, 85f/255f, 1f),
        entityColor = Color(80f/255f, 120f/255f, 160f/255f, 1f),
        shaderEffect = ShaderEffect.UNDERWATER
    ),

    FOREST(
        backgroundFarColor = Color(30f/255f, 50f/255f, 30f/255f, 0.8f),
        backgroundMidColor = Color(40f/255f, 60f/255f, 40f/255f, 0.9f),
        backgroundCloseColor = Color(50f/255f, 70f/255f, 50f/255f, 1f),
        groundColor = Color(55f/255f, 75f/255f, 55f/255f, 1f),
        entityColor = Color(100f/255f, 140f/255f, 100f/255f, 1f),
        shaderEffect = ShaderEffect.FOREST
    ),

    DESERT(
        backgroundFarColor = Color(200f/255f, 180f/255f, 100f/255f, 0.8f),
        backgroundMidColor = Color(210f/255f, 190f/255f, 110f/255f, 0.9f),
        backgroundCloseColor = Color(220f/255f, 200f/255f, 120f/255f, 1f),
        groundColor = Color(225f/255f, 205f/255f, 125f/255f, 1f),
        entityColor = Color(240f/255f, 220f/255f, 140f/255f, 1f),
        shaderEffect = ShaderEffect.DESERT
    ),

    SNOW(
        backgroundFarColor = Color(200f/255f, 200f/255f, 220f/255f, 0.8f),
        backgroundMidColor = Color(210f/255f, 210f/255f, 230f/255f, 0.9f),
        backgroundCloseColor = Color(220f/255f, 220f/255f, 240f/255f, 1f),
        groundColor = Color(225f/255f, 225f/255f, 245f/255f, 1f),
        entityColor = Color(240f/255f, 240f/255f, 255f/255f, 1f),
        shaderEffect = ShaderEffect.SNOW
    );
}

