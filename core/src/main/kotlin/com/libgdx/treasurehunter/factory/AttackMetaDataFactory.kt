package com.libgdx.treasurehunter.factory

import com.badlogic.gdx.graphics.g2d.Animation
import com.libgdx.treasurehunter.ecs.components.AttackMetaData
import com.libgdx.treasurehunter.ecs.components.AttackType
import com.libgdx.treasurehunter.utils.GameObject
import ktx.app.gdxError

object AttackMetaDataFactory {

    fun create(
        gameObject: GameObject,
    ): AttackMetaData {
        return when (gameObject) {
            GameObject.FIERCE_TOOTH -> {
                AttackMetaData(
                    attackSpeed = 1f,
                    attackRange = 1f,
                    attackType = AttackType.FIERCE_TOOTH_ATTACK,
                    attackDamage = 1,
                    baseAttackCooldown = 0f,
                    baseAttackDestroyTime = 1.5f,
                    attackAnimPlayMode = Animation.PlayMode.NORMAL,
                    createFrameIndex = 0,
                    gameObject = gameObject
                )
            }

            GameObject.CRABBY -> {
                AttackMetaData(
                    attackSpeed = 1f,
                    attackRange = 1f,
                    attackType = AttackType.CRABBY_ATTACK,
                    attackDamage = 1,
                    baseAttackCooldown = 1f,
                    baseAttackDestroyTime = 1.5f,
                    attackAnimPlayMode = Animation.PlayMode.NORMAL,
                    createFrameIndex = 0,
                    gameObject = gameObject
                )
            }

            GameObject.PINK_STAR -> {
                AttackMetaData(
                    attackSpeed = 1f,
                    attackRange = 1.5f,
                    attackType = AttackType.PINK_STAR_ATTACK,
                    attackDamage = 1,
                    baseAttackCooldown = 2f,
                    baseAttackDestroyTime = 1.5f,
                    attackAnimPlayMode = Animation.PlayMode.LOOP,
                    createFrameIndex = 0,
                    gameObject = gameObject
                )
            }

            GameObject.SWORD -> {
                AttackMetaData(
                    attackSpeed = 1f,
                    attackRange = 0f,
                    attackType = AttackType.ATTACK_1,
                    attackDamage = 1,
                    baseAttackCooldown = 0.5f,
                    baseAttackDestroyTime = 5f,
                    attackAnimPlayMode = Animation.PlayMode.NORMAL,
                    createFrameIndex = 1,
                    gameObject = gameObject
                )
            }

            GameObject.TOTEM_HEAD_1,
            GameObject.TOTEM_HEAD_2,
            GameObject.TOTEM_HEAD_3,
            GameObject.TOTEM_HEAD_4,
            GameObject.TOTEM_HEAD_5,
            GameObject.TOTEM_HEAD_6 -> {
                AttackMetaData(
                    attackSpeed = 1f,
                    attackRange = 8f,
                    attackType = AttackType.ATTACK,
                    attackDamage = 1,
                    baseAttackCooldown = 1.5f,
                    baseAttackDestroyTime = 5f,
                    attackAnimPlayMode = Animation.PlayMode.NORMAL,
                    createFrameIndex = 3,
                    gameObject = GameObject.WOOD_SPIKE
                )
            }

            else -> gdxError("Unknown game object for attack meta data$gameObject")
        }
    }
}
