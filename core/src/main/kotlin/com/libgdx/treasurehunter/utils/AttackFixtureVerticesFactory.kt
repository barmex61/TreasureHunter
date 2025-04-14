package com.libgdx.treasurehunter.utils

import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.Shape
import com.libgdx.treasurehunter.ecs.components.AttackType
import com.libgdx.treasurehunter.utils.Constants.OBJECT_FIXTURES
import ktx.app.gdxError
import ktx.assets.disposeSafely

object AttackFixtureVerticesFactory {
    fun getMeleeAttackVertices(attackType: AttackType): Map<Int, FloatArray> {
        return when(attackType){
            AttackType.ATTACK_1 -> mapOf(
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
            )

            AttackType.ATTACK_2 -> mapOf(
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
            )

            AttackType.ATTACK_3 -> mapOf(
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
            else -> gdxError("Unknown attack type for melee attack fixture vertices: $attackType")
        }
    }

    fun getRangedAttackVertices(attackType: AttackType):Map<Int, ChainShape> {
        return when(attackType){
            AttackType.THROW -> {
                val chainShape = OBJECT_FIXTURES[GameObject.SWORD_EMBEDDED]!!.first().fixtureDef.shape as ChainShape
                mapOf(
                    0 to chainShape
                )
            }
            else -> gdxError("Unknown attack type for ranged attack fixture vertices: $attackType")
        }
    }
}
