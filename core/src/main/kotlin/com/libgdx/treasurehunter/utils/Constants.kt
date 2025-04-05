package com.libgdx.treasurehunter.utils

import com.badlogic.gdx.math.Vector2

object Constants {

    const val UNIT_SCALE = 1/32f
    val GRAVITY = Vector2(0f,-9.8f)
    val OBJECT_FIXTURES = mutableMapOf<GameObject, List<FixtureDefUserData>>()
}
