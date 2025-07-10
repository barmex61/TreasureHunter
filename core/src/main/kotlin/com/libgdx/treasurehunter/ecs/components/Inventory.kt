package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity


data class Inventory(
    val items : MutableList<ItemData> = mutableListOf(),
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
