package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.utils.GameObject
import ktx.app.gdxError

enum class AttackType(val isMelee: Boolean) {
    ATTACK_1(true),
    ATTACK_2(true),
    ATTACK_3(true),
    AIR_ATTACK_1(true),
    AIR_ATTACK_2(true),
    THROW(false),;
}

sealed interface ItemType{
    interface Damageable : ItemType {
        val attackSpeed: Float
        val attackRange: Float
        var attackType: AttackType
        val attackDamage : Int
        var attackCooldown : Float
        var attackDestroyTime : Float
        val isMelee : Boolean

        val attackAnimType : AnimationType
            get() = AnimationType.valueOf(attackType.name)

        fun toGameObject() : GameObject?{
            return when(this){
                is Sword -> GameObject.SWORD
                else -> null
            }
        }
    }

    data class Sword(
        override val attackSpeed: Float = 1f,
        override val attackRange: Float = 0f,
        override var attackType: AttackType = AttackType.ATTACK_1,
        override val attackDamage: Int = 1,
        override var attackCooldown: Float = 1.5f,
        override var attackDestroyTime: Float = 1.5f,

    ) : Damageable {
        override val isMelee: Boolean
            get() = attackType.isMelee
    }

    data class Bomb(
        override val attackSpeed: Float,
        override val attackRange: Float,
        override var attackType: AttackType,
        override val attackDamage: Int = 3,
        override var attackCooldown: Float = 2f,
        override var attackDestroyTime: Float = 2f,
        override val isMelee: Boolean = false
    ) : Damageable

    data class Consumable(
        val effect: String
    ) : ItemType
}

data class Item(
    val itemType: ItemType,
    val owner: Entity,
    val isPickedUp: Boolean = false,
    val isUsed: Boolean = false,
    val isEquipped: Boolean = false,
    val isDropped: Boolean = false,
    val isInInventory: Boolean = false
) : Component <Item> {

    override fun type() = Item

    companion object : ComponentType<Item>()

}
