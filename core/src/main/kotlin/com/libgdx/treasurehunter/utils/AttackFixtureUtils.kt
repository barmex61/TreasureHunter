package com.libgdx.treasurehunter.utils

import com.libgdx.treasurehunter.ecs.components.AttackType

val playerAttackVertices: Map<AttackType, FloatArray> = mapOf(
    AttackType.ATTACK_1 to floatArrayOf(
        0.25f,-0.3f,
        0.25f,-0.15f,
        0.8f,-0.15f,
        0.8f,-0.3f,
    ),

    AttackType.ATTACK_2 to floatArrayOf(
        0.1f, 0.2f,
        0.2f, 0.3f,
        0.3f, 0.2f,
        0.5f, 0.4f,
        0.8f, 0.1f,
        1.0f, -0.2f,
        0.9f, -0.3f,
        0.7f, -0.1f,
        0.4f, 0.1f,
        0.3f, 0.2f
    ),

    AttackType.ATTACK_3 to floatArrayOf(
        -0.1f, -0.1f,
        0.0f, 0.1f,
        0.1f, 0.2f,
        0.4f, 0.3f,
        0.7f, 0.2f,
        1.0f, -0.1f,
        0.7f, -0.4f,
        0.4f, -0.5f,
        0.1f, -0.4f,
        0.0f, -0.3f,
        -0.1f, -0.1f
    )
)
