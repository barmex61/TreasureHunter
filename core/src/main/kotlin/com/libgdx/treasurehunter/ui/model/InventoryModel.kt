package com.libgdx.treasurehunter.ui.model

import com.libgdx.treasurehunter.ecs.components.ItemData
import com.libgdx.treasurehunter.ecs.components.SlotName
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventListener
import com.libgdx.treasurehunter.event.GameEvent.EquippedItemChanged

class InventoryModel (
    var onItemChange : (List<ItemData>) -> Unit = {},
    var onEquippedItemChange: (String, ItemData?) -> Unit = { _, _ -> }
) : GameEventListener {

    var equippedSword: ItemData? = null
    var equippedArmor: ItemData? = null
    var equippedBoots: ItemData? = null
    var equippedHelmet: ItemData? = null

    override fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.InventoryChangeEvent ->{
                onItemChange(event.inventoryItems)
            }
            is EquippedItemChanged -> {
                when (event.slotName) {
                    SlotName.SWORD.toString() -> equippedSword = event.item
                    SlotName.ARMOR.toString() -> equippedArmor = event.item
                    SlotName.BOOTS.toString() -> equippedBoots = event.item
                    SlotName.HELMET.toString() -> equippedHelmet = event.item
                }
                onEquippedItemChange(event.slotName, event.item)
            }
            else -> Unit
        }
    }
}
