package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.Inventory
import com.libgdx.treasurehunter.ecs.components.ItemData
import com.libgdx.treasurehunter.ecs.components.ItemType
import com.libgdx.treasurehunter.ecs.components.SlotName
import com.libgdx.treasurehunter.ecs.components.Sword
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEvent.EquipItemRequest
import com.libgdx.treasurehunter.event.GameEventListener

class InventorySystem : IteratingSystem(
    family = family { all(Inventory) }
), GameEventListener {
    override fun onTickEntity(entity: Entity) {
        val inventory = entity[Inventory]
        val equippedSword = inventory.equippedSword
        val attackComp = entity.getOrNull(Attack)
        if (equippedSword != null && equippedSword.itemType is ItemType.Damageable) {
            if (attackComp == null) {
                entity.configure { it += Attack(attackMetaData = equippedSword.itemType.attackMetaData) }
            }
        } else {
            if (attackComp != null) {
                entity.configure { it -= Attack }
            }
        }
        if (entity has Attack){
            println("Entity has attack")
        }
    }

    override fun onEvent(event: GameEvent) {

        when (event) {
            is EquipItemRequest -> {
                val inventory = event.item.owner!![Inventory]
                handleEquipOrSwap(
                    inventory = inventory,
                    slotName = event.slotName,
                    newItem = event.item
                )
            }
            is GameEvent.UnEquipItemRequest -> {
                val inventory = event.item.owner!![Inventory]
                handleEquipItemRemoved(event.slotName, inventory)
                inventory.addItem(event.item)
            }
            is GameEvent.EquippedItemRemoved -> {
                val inventory = event.entity[Inventory]
                handleEquipItemRemoved(event.slotName,inventory)
            }
            else -> Unit
        }
    }
    private fun handleEquipOrSwap(
        inventory: Inventory,
        slotName: String,
        newItem: ItemData
    ) {
        val (getter, setter) = when (slotName) {
            SlotName.SWORD.toString() -> Pair({ inventory.equippedSword }, { v: ItemData? -> inventory.equippedSword = v })
            SlotName.HELMET.toString() -> Pair({ inventory.equippedHelmet }, { v: ItemData? -> inventory.equippedHelmet = v })
            SlotName.ARMOR.toString() -> Pair({ inventory.equippedArmor }, { v: ItemData? -> inventory.equippedArmor = v })
            SlotName.BOOTS.toString() -> Pair({ inventory.equippedBoots }, { v: ItemData? -> inventory.equippedBoots = v })
            else -> return
        }

        val equippedItem = getter()
        if (equippedItem == null) {
            inventory.removeItem(newItem)
            setter(newItem)
        } else {
            inventory.addItem(equippedItem)
            inventory.removeItem(newItem)
            setter(newItem)
        }
    }

    private fun handleEquipItemRemoved(slotName: String, inventory: Inventory) {
        when (slotName) {
            SlotName.SWORD.toString() -> inventory.equippedSword = null
            SlotName.HELMET.toString() -> inventory.equippedHelmet = null
            SlotName.ARMOR.toString() -> inventory.equippedArmor = null
            SlotName.BOOTS.toString() -> inventory.equippedBoots = null

        }
    }

}
