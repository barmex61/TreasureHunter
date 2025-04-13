package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.Item
import com.libgdx.treasurehunter.ecs.components.ItemType

class ItemSystem : IteratingSystem(
    family = family{all(Item)}
) {

    override fun onTickEntity(entity: Entity) {
        val itemComp = entity[Item]
        val itemType = itemComp.itemType
        val damageableItem = itemType as? ItemType.Damageable
        if (damageableItem != null){
            entity.configure {
                if (entity hasNo Attack){
                    entity += Attack(
                        attackMetaData = damageableItem.attackMetaData
                    )
                }
            }
        }
    }
}
