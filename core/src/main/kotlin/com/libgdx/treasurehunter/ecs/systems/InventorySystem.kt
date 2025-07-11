package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.Inventory
import com.libgdx.treasurehunter.ecs.components.ItemType
import com.libgdx.treasurehunter.ecs.components.ItemData
import com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.ecs.components.Sword
import com.libgdx.treasurehunter.state.EntityState
import com.libgdx.treasurehunter.state.PlayerState
import com.libgdx.treasurehunter.state.StateEntity
import com.libgdx.treasurehunter.utils.GameObject
import com.libgdx.treasurehunter.utils.animation

class InventorySystem : IteratingSystem(
    family = family { all(Inventory) }
) {
    override fun onTickEntity(entity: Entity) {
        val inventory = entity[Inventory]
        val equippedSword = inventory.equippedSword
        if (inventory.equippedSword == null){
            val swordItem = inventory.items.firstOrNull { it.itemType is Sword } ?: return
            inventory.equippedSword = swordItem
            entity[Animation].modelName = GameObject.CAPTAIN_CLOWN_SWORD.atlasKey

        }

        if (equippedSword != null && equippedSword.itemType is ItemType.Damageable) {

            if (entity.hasNo(Attack)) {
                entity.configure { it += Attack(attackMetaData = equippedSword.itemType.attackMetaData).also { it.equippedItem = equippedSword.itemType } }
            }
        } else {
            if (entity.has(Attack)) {
                entity.configure { it -= Attack }
            }
        }
    }
}
