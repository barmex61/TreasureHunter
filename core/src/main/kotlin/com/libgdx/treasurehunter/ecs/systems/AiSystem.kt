package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.AiComponent
import kotlin.collections.get


class AiSystem(

) : IteratingSystem(
    family = family { all(AiComponent) }
){

    override fun onTickEntity(entity: Entity) {
        val aiComponent = entity[AiComponent]
        aiComponent.run {
            behaviorTree.step()
        }
    }
}
