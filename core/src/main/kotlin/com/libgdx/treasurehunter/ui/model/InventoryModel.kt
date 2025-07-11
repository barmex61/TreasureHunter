package com.libgdx.treasurehunter.ui.model

import com.libgdx.treasurehunter.ecs.components.ItemData
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventListener

class InventoryModel (
    val items: MutableList<ItemData> = mutableListOf(),
    var onItemChange : (List<ItemData>) -> Unit = {}
): GameEventListener {

    override fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.InventoryChangeEvent ->{
                items.clear()
                items.addAll(event.inventoryItems)
                onItemChange(items)
            }
            else -> Unit
        }
    }
}
