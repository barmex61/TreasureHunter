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
enum class AttackType(val animType : AnimationType,val attackDestroyCooldown : Float) {
    ATTACK_1(AnimationType.ATTACK_1,1f),
    ATTACK_2(AnimationType.ATTACK_2,1f),
    ATTACK_3(AnimationType.ATTACK_3,1f),
    AIR_ATTACK_1(AnimationType.AIR_ATTACK_1,1f),
    AIR_ATTACK_2(AnimationType.AIR_ATTACK_2,1f),
    THROW_ATTACK(AnimationType.THROW,3f);

}

enum class AttackItem(val itemDamage : Int){
    NONE(0),
    SWORD(1);
    companion object{
        fun AttackItem.toGameObject() : GameObject?{
            return when(this){
                NONE -> null
                SWORD -> GameObject.SWORD
            }
        }
    }
}

data class Attack(
    var attackItem : AttackItem = AttackItem.NONE,
    var wantsToAttack : Boolean = false,
    var attackState : AttackState = AttackState.READY,
    var attackType : AttackType = AttackType.ATTACK_1,
    var attackCooldown : Float = 1f
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
