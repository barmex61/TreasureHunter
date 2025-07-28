package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.utils.GameObject
import kotlin.reflect.KClass

enum class SlotName {
    SWORD, ARMOR, BOOTS, HELMET;

    override fun toString(): String {
        return this.name.lowercase()
    }
}
data class Inventory(
    val owner: Entity,
) : Component <Inventory> {
    private var _items: MutableList<ItemData> = mutableListOf()
    val items: List<ItemData>
        get() = _items

    fun addItem(item: ItemData) {
        val itemGroup = _items.groupBy { it.itemType }
        if (itemGroup[item.itemType] != null && itemGroup[item.itemType]?.size!! >= 9) return
        _items.add(item)
        GameEventDispatcher.fireEvent(GameEvent.InventoryChangeEvent(_items.toList()))
    }

    fun removeItem(item: ItemData) {
        val lastIndex = _items.indexOfLast { it.itemType == item.itemType }
        if (lastIndex != -1) {
            _items.removeAt(lastIndex)
            GameEventDispatcher.fireEvent(GameEvent.InventoryChangeEvent(_items.toList()))
        }
    }

    fun removeItemByType(itemType: ItemType) {
        val lastIndex = _items.indexOfLast { it.itemType == itemType }
        if (lastIndex != -1) {
            _items.removeAt(lastIndex)
            GameEventDispatcher.fireEvent(GameEvent.InventoryChangeEvent(_items.toList()))
        }
    }

    private var _equippedSword: ItemData? = null
    var equippedSword: ItemData?
        get() = _equippedSword
        set(value) {
            _equippedSword = value
            val modelName = if (value == null){
                GameObject.CAPTAIN_CLOWN.atlasKey
            } else GameObject.CAPTAIN_CLOWN_SWORD.atlasKey
            GameEventDispatcher.fireEvent(GameEvent.EntityModelChangeEvent(owner, modelName))
            GameEventDispatcher.fireEvent(GameEvent.EquippedItemChanged(SlotName.SWORD.toString(), value))
        }

    private var _equippedArmor: ItemData? = null
    var equippedArmor: ItemData?
        get() = _equippedArmor
        set(value) {
            _equippedArmor = value
            GameEventDispatcher.fireEvent(GameEvent.EquippedItemChanged(SlotName.ARMOR.toString(), value))
        }

    private var _equippedBoots: ItemData? = null
    var equippedBoots: ItemData?
        get() = _equippedBoots
        set(value) {
            _equippedBoots = value
            GameEventDispatcher.fireEvent(GameEvent.EquippedItemChanged(SlotName.BOOTS.toString(), value))
        }

    private var _equippedHelmet: ItemData? = null
    var equippedHelmet: ItemData?
        get() = _equippedHelmet
        set(value) {
            _equippedHelmet = value
            GameEventDispatcher.fireEvent(GameEvent.EquippedItemChanged(SlotName.HELMET.toString(), value))
        }

    fun hasItem(itemTypeClass: KClass<out ItemType>): Boolean {
        return _items.any { it.itemType::class == itemTypeClass }
    }

    override fun type() = Inventory

    companion object : ComponentType<Inventory>()

}
