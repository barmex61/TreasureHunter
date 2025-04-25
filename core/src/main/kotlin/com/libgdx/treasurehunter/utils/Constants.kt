package com.libgdx.treasurehunter.utils

import com.badlogic.gdx.math.Vector2
import com.libgdx.treasurehunter.ecs.components.AttackType

object Constants {

    const val UNIT_SCALE = 1/32f
    val GRAVITY = Vector2(0f,-9.8f)
    val OBJECT_FIXTURES = mutableMapOf<GameObject, List<FixtureDefUserData>>()
    val ATTACK_FIXTURES = mutableMapOf< Pair<AttackType, Int>, List<FixtureDefUserData>>()
    val ATTACK_EFFECT_FIXTURES = mutableMapOf<Pair<AttackType, Int>, List<FixtureDefUserData>>()
    var currentMapPath : String = ""
}
