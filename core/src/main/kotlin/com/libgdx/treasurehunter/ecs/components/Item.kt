package com.libgdx.treasurehunter.ecs.components

import com.libgdx.treasurehunter.ecs.components.ItemType.Damageable
import com.libgdx.treasurehunter.ecs.components.ItemType.Throwable
import com.libgdx.treasurehunter.factory.AttackMetaDataFactory
import com.libgdx.treasurehunter.utils.GameObject

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import ktx.app.gdxError

data class Item(
    val itemData : ItemData,
) : Component <Item> {

    override fun type() = Item

    companion object : ComponentType<Item>()

}

data class ItemData(
    val itemType: ItemType,
    var owner : Entity? = null
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
    interface Equippable : ItemType
    interface Consumable : ItemType

    interface Collectable : ItemType
    fun toDrawablePath() : String = when(this){
        is Sword -> "sword_on"
        is Map -> {
            when(mapType){
                MapType.BIG_MAP -> "big_map_1"
                MapType.SMALL_MAP_1 -> "small_map_1"
                MapType.SMALL_MAP_2 -> "small_map_2"
                MapType.SMALL_MAP_3 -> "small_map_3"
                MapType.SMALL_MAP_4 -> "small_map_4"
            }
        }
        is Key -> "key"
        else -> gdxError("ItemType $this does not have a drawable path defined")
    }
}

data class Sword(
    override val attackMetaData: AttackMetaData = AttackMetaDataFactory.create(GameObject.SWORD),
    override var throwState: ThrowState = ThrowState.READY
) : Damageable,Throwable, ItemType.Equippable

data class Projectile(
    val projectileType: ProjectileType,
    override val attackMetaData: AttackMetaData = AttackMetaDataFactory.create(projectileType.toGameObject()),
    override var throwState: ThrowState = ThrowState.READY
) : Damageable, Throwable

data class Map (
    val mapType : MapType
) : ItemType.Collectable


class Armor : ItemType.Equippable
class Helmet : ItemType.Equippable

class Boots : ItemType.Equippable

class Shield : ItemType.Equippable

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


