package com.libgdx.treasurehunter.ecs.components

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
            is Projectile -> this.projectileType.toGameObject()
            else -> null
        }
    }
}

data class Sword(
    override val attackMetaData: AttackMetaData = AttackMetaDataFactory.create(GameObject.SWORD),
    override var throwState: ThrowState = ThrowState.READY
) : Damageable,Throwable

data class Projectile(
    val projectileType: ProjectileType,
    override val attackMetaData: AttackMetaData = AttackMetaDataFactory.create(projectileType.toGameObject()),
    override var throwState: ThrowState = ThrowState.READY
) : Damageable, Throwable


enum class ProjectileType{
    WOOD_SPIKE;
    fun toGameObject() : GameObject{
        return when(this){
            WOOD_SPIKE -> GameObject.WOOD_SPIKE
        }
    }
}

data class Item(
    var itemType: ItemType,
    val owner: Entity,
    val isPickedUp: Boolean = false,
    val isUsed: Boolean = false,
    ) : Component <Item> {

    override fun type() = Item

    companion object : ComponentType<Item>()

}
