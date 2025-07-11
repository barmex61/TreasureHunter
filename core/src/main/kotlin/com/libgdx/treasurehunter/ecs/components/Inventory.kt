package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher

enum class EquipSlot { SWORD, ARMOR, BOOTS, HELMET }

data class Inventory(
    val items: MutableList<ItemData> = mutableListOf(),
    var equippedSword: ItemData? = null,
    var equippedArmor: ItemData? = null,
    var equippedBoots: ItemData? = null,
    var equippedHelmet: ItemData? = null,
    val owner: Entity,
) : Component <Inventory> {

    override fun type() = Inventory

    companion object : ComponentType<Inventory>()

    fun addItem(item: ItemData): Boolean {
        items.add(item)
        GameEventDispatcher.fireEvent(GameEvent.InventoryChangeEvent(items.toList()))
        return true
    }
}
