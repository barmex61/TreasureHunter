package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.Inventory
import com.libgdx.treasurehunter.ecs.components.ItemType
import com.libgdx.treasurehunter.ecs.components.Sword
import com.libgdx.treasurehunter.utils.GameObject

class InventorySystem : IteratingSystem(
    family = family { all(Inventory) }
) {
    override fun onTickEntity(entity: Entity) {
        val inventory = entity[Inventory]
        val equippedSword = inventory.items.firstOrNull { it.itemType is Sword }
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
    }
}
