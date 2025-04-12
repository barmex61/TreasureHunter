package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.physics.box2d.Body
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.utils.GameObject

enum class AttackState {
    READY,
    ATTACKING,
    DONE,
}

//NEED TO IMPROVE ATTACK NAMES I DIDNT KNOW WHAT TO PUT.NEED TO CHECK WHAT KIND OF ANIMATIONS THEY ARE

data class Attack(
    var wantsToAttack : Boolean = false,
    var attackState : AttackState = AttackState.READY,
    var doAttack : Boolean = false
    ) : Component <Attack> {


    val isMeleeAttack : Boolean
        get() {
            return when(this.attackType){
                AttackType.THROW_ATTACK -> false
                else -> true
            }
        }

    val attackDamage : Int
        get() {
            return when(this.attackItem){
                AttackItem.NONE -> 0
                AttackItem.SWORD -> {
                    when(this.attackType){
                        AttackType.ATTACK_1 -> 1
                        AttackType.ATTACK_2 -> 1
                        AttackType.ATTACK_3 -> 2
                        AttackType.AIR_ATTACK_1 -> 1
                        AttackType.AIR_ATTACK_2 -> 1
                        AttackType.THROW_ATTACK -> 2
                    }
                }
            }
        }


    override fun type(): ComponentType<Attack> = Attack

    companion object : ComponentType<Attack>()

}
