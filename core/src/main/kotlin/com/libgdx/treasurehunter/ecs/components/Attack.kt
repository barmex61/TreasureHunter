package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

enum class AttackState {
    READY,
    ATTACKING,
    DONE,
}

enum class AttackData(val attackMetaData: AttackMetaData){
    PINK_STAR(
        AttackMetaData(
            attackSpeed = 1f,
            attackRange = 1f,
            attackType = AttackType.NATURAL,
            attackDamage = 1,
            baseAttackCooldown = 1.5f,
            baseAttackDestroyTime = 1.5f,
        )
    ),
    FIERCE_TOOTH(
        AttackMetaData(
            attackSpeed = 1f,
            attackRange = 1f,
            attackType = AttackType.NATURAL,
            attackDamage = 1,
            baseAttackCooldown = 1.5f,
            baseAttackDestroyTime = 1.5f,
        )
    ),
    CRABBY(
        AttackMetaData(
            attackSpeed = 1f,
            attackRange = 1f,
            attackType = AttackType.NATURAL,
            attackDamage = 1,
            baseAttackCooldown = 1.5f,
            baseAttackDestroyTime = 1.5f,
        )
    )
}

data class AttackMetaData(
    val attackSpeed: Float ,
    val attackRange: Float,
    var attackType: AttackType,
    val attackDamage: Int ,
    val baseAttackCooldown : Float,
    var attackCooldown: Float = baseAttackCooldown,
    val baseAttackDestroyTime : Float,
    var attackDestroyTime: Float = baseAttackDestroyTime,
){
    val isMelee: Boolean
        get() = attackType.isMelee

    fun resetAttackCooldown(){
        attackCooldown = baseAttackCooldown
    }

    fun resetAttackDestroyTime(){
        attackDestroyTime = baseAttackDestroyTime
    }
}

data class Attack(
    var wantsToAttack : Boolean = false,
    var attackState : AttackState = AttackState.READY,
    var doAttack : Boolean = false,
    val attackMetaData: AttackMetaData,
    ) : Component <Attack> {

    var queuedAttackType : AttackType? = null

    override fun type(): ComponentType<Attack> = Attack

    companion object : ComponentType<Attack>()

}
