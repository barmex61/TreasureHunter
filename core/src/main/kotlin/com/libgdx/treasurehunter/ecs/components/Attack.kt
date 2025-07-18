package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.utils.GameObject
import ktx.math.vec2

enum class AttackState {
    READY,
    ATTACKING,
    DONE,
}


enum class AttackType(val isMelee : Boolean,val attackOffset : Vector2 = vec2(-0.3f,0.15f),val knockbackVector : Vector2 = vec2(0f,0f)) {
    ATTACK(false, attackOffset = vec2(-0.6f,-0.5f)),
    ATTACK_1(true, knockbackVector = vec2(1.5f,0f)),
    ATTACK_2(true, knockbackVector = vec2(0f,-1.5f)),
    ATTACK_3(true, knockbackVector = vec2(0f,1.5f)),
    AIR_ATTACK_1(true),
    AIR_ATTACK_2(true),
    FIERCE_TOOTH_ATTACK(true,vec2(1f,0.15f)),
    PINK_STAR_ATTACK(true),
    CRABBY_ATTACK(true,vec2(1.7f,0f)),
    THROW(false,vec2(0f,-0.2f))
    ;

    val attackAnimType : AnimationType
        get() = AnimationType.valueOf(this.name)
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
    val attackAnimPlayMode : Animation.PlayMode,
    val createFrameIndex : Int,
    val gameObject: GameObject
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
    var attackMetaData: AttackMetaData,
    ) : Component <Attack> {


    var queuedAttackType : AttackType? = null

    override fun type(): ComponentType<Attack> = Attack

    companion object : ComponentType<Attack>()

}
