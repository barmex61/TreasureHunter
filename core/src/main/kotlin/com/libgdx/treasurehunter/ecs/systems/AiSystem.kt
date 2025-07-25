package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.AiComponent
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventListener
import kotlin.collections.get


class AiSystem(

) : IteratingSystem(
    family = family { all(AiComponent) }
), GameEventListener{

    override fun onTickEntity(entity: Entity) {
        val aiComponent = entity[AiComponent]
        aiComponent.run {
            behaviorTree.step()
        }
    }

    override fun onEvent(event: GameEvent) {
        when(event){
            is GameEvent.PlayerDeadEvent -> {
                val playerEntity = event.playerEntity
                val aiEntities = with(world){  family { all(AiComponent) }  }
                aiEntities.forEach { aiEntity->
                    val aiComp = aiEntity[AiComponent]
                    aiComp.nearbyEntities.remove(playerEntity)
                }
            }
            else -> Unit
        }
    }
}
