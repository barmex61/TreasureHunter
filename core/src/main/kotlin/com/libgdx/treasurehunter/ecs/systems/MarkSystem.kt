package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Mark
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.utils.plus

class MarkSystem : IteratingSystem(
    family = family { all(Mark, Graphic, Animation) },
) {

    override fun onTickEntity(entity: Entity) {
        val markComp = entity[Mark]
        val graphicComp = entity[Graphic]
        val (sprite, _) = graphicComp
        var (markDuration, markOffset,markedEntity) = markComp
        val markedEntityGraphicCenter = markedEntity.getOrNull(Graphic)?.center
        if (markedEntityGraphicCenter == null){
            GameEventDispatcher.fireEvent(GameEvent.RemoveEntityEvent(entity))
            return
        }
        if (markDuration <= 0f){
            GameEventDispatcher.fireEvent(GameEvent.RemoveEntityEvent(entity))
            return
        }else{
            markDuration -= deltaTime
            val newPosition = markedEntityGraphicCenter + markOffset
            sprite.setPosition(newPosition.x,newPosition.y)
        }

        markComp.markDuration = markDuration
    }
}
