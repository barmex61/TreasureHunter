package com.libgdx.treasurehunter.ecs.components

import com.libgdx.treasurehunter.ecs.components.ItemType.Damageable
import com.libgdx.treasurehunter.ecs.components.ItemType.Throwable
import com.libgdx.treasurehunter.factory.AttackMetaDataFactory
import com.libgdx.treasurehunter.utils.GameObject

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

data class Item(
    val itemData : ItemData,
) : Component <Item> {

    override fun type() = Item

    companion object : ComponentType<Item>()

}

data class ItemData(
    val itemType: ItemType,
    val owner : Entity? = null
)

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
    interface Consumable : ItemType{

    }

    interface Collectable : ItemType

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

data class Map (
    val mapType : MapType
) : ItemType.Collectable

class Diamond : ItemType.Collectable

class Skull : ItemType.Collectable

class Key : ItemType.Collectable


enum class MapType {
    BIG_MAP,
    SMALL_MAP_1,
    SMALL_MAP_2,
    SMALL_MAP_3,
    SMALL_MAP_4
}
enum class ProjectileType{
    WOOD_SPIKE;
    fun toGameObject() : GameObject{
        return when(this){
            WOOD_SPIKE -> GameObject.WOOD_SPIKE
        }
    }
}


