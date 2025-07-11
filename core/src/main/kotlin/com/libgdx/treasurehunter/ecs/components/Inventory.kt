package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

enum class EquipSlot { SWORD, ARMOR, BOOTS, HELMET }

data class Inventory(
    val items: MutableList<ItemData> = mutableListOf(),
    var equippedSword: ItemData? = null,
    var equippedArmor: ItemData? = null,
    var equippedBoots: ItemData? = null,
    var equippedHelmet: ItemData? = null,
    val owner: Entity,
    val maxSize: Int = 25,
) : Component <Inventory> {

    override fun type() = Inventory

    companion object : ComponentType<Inventory>()

    fun addItem(item: ItemData): Boolean {
        if (items.size >= maxSize) return false
        items.add(item)
        return true
    }
}
