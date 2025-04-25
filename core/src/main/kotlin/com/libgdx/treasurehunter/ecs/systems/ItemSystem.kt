package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.Item
import com.libgdx.treasurehunter.ecs.components.ItemType
import com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.ecs.components.ThrowState
import com.libgdx.treasurehunter.state.EntityState
import com.libgdx.treasurehunter.state.PlayerState
import com.libgdx.treasurehunter.state.StateEntity

class ItemSystem : IteratingSystem(
    family = family{all(Item)}
) {

    override fun onTickEntity(entity: Entity) {
        val itemComp = entity[Item]
        val itemType = itemComp.itemType
        val damageableItem = itemType as? ItemType.Damageable
        val throwableItem = itemType as? ItemType.Throwable
        if (damageableItem != null){
            entity.configure {
                if (entity hasNo Attack){
                    entity += Attack(
                        attackMetaData = damageableItem.attackMetaData
                    )
                }
            }
        }
        if (throwableItem != null){
            when(throwableItem.throwState){
                ThrowState.THROWED ->  entity.getOrNull(State)?.stateMachine?.changeState(PlayerState.SWORD_THROWED as EntityState<StateEntity>)
                else -> Unit
            }

        }

    }
}
