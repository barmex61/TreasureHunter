package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.utils.GameObject

enum class AttackType(val animType : AnimationType,val attackDestroyCooldown : Float,val isMelee : Boolean) {
    ATTACK_1(AnimationType.ATTACK_1,1f,true),
    ATTACK_2(AnimationType.ATTACK_2,1f,true),
    ATTACK_3(AnimationType.ATTACK_3,1f,true),
    AIR_ATTACK_1(AnimationType.AIR_ATTACK_1,1f,true),
    AIR_ATTACK_2(AnimationType.AIR_ATTACK_2,1f,true),
    THROW_ATTACK(AnimationType.THROW,3f,false),;

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

sealed interface ItemType{
    data class Weapon(
        val attackSpeed: Float,
        val attackRange: Float,
        val attackType: AttackType,
        val attackItem: AttackItem
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
