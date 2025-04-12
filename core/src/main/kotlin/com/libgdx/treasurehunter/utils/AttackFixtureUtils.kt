package com.libgdx.treasurehunter.utils

import com.libgdx.treasurehunter.ecs.components.AttackType
import com.libgdx.treasurehunter.utils.Constants.OBJECT_FIXTURES

val playerSwordMeleeAttackVertices: Map<AttackType, Map<Int,FloatArray> > = mapOf(

    AttackType.ATTACK_1 to mapOf(
        0 to floatArrayOf(
            0.25f,-0.25f,
            0.25f,-0.1f,
            0.65f,-0.1f,
            0.5f,-0.25f,
        ),
        1 to floatArrayOf(
            0.6f,-0.25f,
            0.6f,-0.1f,
            1f,-0.1f,
            0.85f,-0.25f,
        )
    ),

    AttackType.ATTACK_2 to mapOf(
        0 to floatArrayOf(
            0.4f, 0.18f,
            0.25f, 0.18f,
            0.25f, 0.58f,
            0.4f, 0.43f
        ),
        1 to floatArrayOf(
            0.63f, 0.25f,
            0.83f, -0.1f,
            0.88f,-0.25f,
            0.79f, -0.62f,
            0.43f, -0.4f,
        )
    ),

    AttackType.ATTACK_3 to mapOf(
        0 to floatArrayOf(
            -0.1f, -0.25f,
            -0.1f, -0.1f,
            -0.5f, -0.1f,
            -0.35f, -0.25f
        ),
        1 to floatArrayOf(
            0.35f, -0.3f,
            0.7f,-0.1f,
            0.83f, 0.1f,
            0.88f, 0.25f,
            0.79f, 0.62f,
            0.43f, 0.4f
        )
    )
)

val playerSwordRangedAttackVertices = OBJECT_FIXTURES[GameObject.SWORD_EMBEDDED]


fun FloatArray.mirrorVertices(): FloatArray {
    val mirroredVertices = FloatArray(this.size)
    for (i in this.indices step 2) {
        mirroredVertices[i] = -this[i]
        mirroredVertices[i + 1] = this[i + 1]
    }
    return mirroredVertices
}
