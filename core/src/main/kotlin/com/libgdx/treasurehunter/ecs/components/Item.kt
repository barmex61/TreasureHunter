package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.graphics.g2d.Animation
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.ecs.components.ItemType.Damageable
import com.libgdx.treasurehunter.ecs.components.ItemType.Throwable
import com.libgdx.treasurehunter.factory.AttackMetaDataFactory
import com.libgdx.treasurehunter.utils.GameObject

enum class ThrowState{
    READY,
    THROWING,
    THROWED
}

sealed interface ItemType{
    interface Damageable : ItemType {
        val attackMetaData : AttackMetaData
    }
    interface Throwable : ItemType{
        var throwState : ThrowState
    }
    data class Consumable(
        val effect: String
    ) : ItemType

    fun toGameObject() : GameObject?{
        return when(this){
            is Sword -> GameObject.SWORD
            else -> null
        }
    }
}

data class Sword(
    override val attackMetaData: AttackMetaData = AttackMetaDataFactory.create(GameObject.SWORD),
    override var throwState: ThrowState = ThrowState.READY
) : Damageable,Throwable



data class Bomb(
    override val attackMetaData: AttackMetaData = AttackMetaData(
        attackSpeed= 1f,
        attackRange= 0f,
        attackType= AttackType.ATTACK_1,
        attackDamage= 3,
        baseAttackCooldown=2f,
        baseAttackDestroyTime= 2f,
        attackAnimPlayMode = Animation.PlayMode.NORMAL
    )
) : Damageable


data class Item(
    var itemType: ItemType,
    val owner: Entity,
    val isPickedUp: Boolean = false,
    val isUsed: Boolean = false,
    ) : Component <Item> {

    override fun type() = Item

    companion object : ComponentType<Item>()

}
