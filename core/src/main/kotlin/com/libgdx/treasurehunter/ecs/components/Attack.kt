package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.Shape
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.utils.AttackFixtureVerticesFactory

enum class AttackState {
    READY,
    ATTACKING,
    DONE,
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
    val attackAnimPlayMode : Animation.PlayMode
){
    val isMelee: Boolean
        get() = attackType.isMelee

    fun resetAttackCooldown(){
        attackCooldown = baseAttackCooldown
    }

    fun resetAttackDestroyTime(){
        attackDestroyTime = baseAttackDestroyTime
    }

    val meleeAttackFixtureVertices :  Map<Int,FloatArray>
        get() = AttackFixtureVerticesFactory.getMeleeAttackVertices(attackType)
    val rangeAttackFixtureVertices : Map<Int, ChainShape>
        get() = AttackFixtureVerticesFactory.getRangedAttackVertices(attackType)
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
