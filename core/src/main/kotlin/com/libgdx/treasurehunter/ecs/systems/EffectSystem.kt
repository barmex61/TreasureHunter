package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Effect
import com.libgdx.treasurehunter.ecs.components.Remove
import com.libgdx.treasurehunter.utils.plus
import ktx.math.vec2

class EffectSystem : IteratingSystem(
    family = family { all(Effect, Graphic, Animation) },
) {

    override fun onTickEntity(entity: Entity) {
        val effectComp = entity[Effect]
        val graphicComp = entity[Graphic]
        val animComp = entity[Animation]
        val (sprite, _) = graphicComp
        var (effectDuration, effectOffset,startPosition,owner,stickToOwner) = effectComp
        val ownerGraph = owner.getOrNull(Graphic)
        val ownerPosition = if (stickToOwner && ownerGraph != null) ownerGraph.center else null
        val newPosition = ownerPosition ?: startPosition
        if (effectDuration <= 0f || animComp.isAnimationDone()){
            entity.configure {
                it += Remove()
            }
            return
        }else{
            effectDuration -= deltaTime
            val position = newPosition + effectOffset
            sprite.setPosition(position.x,position.y)
        }

        effectComp.effectDuration = effectDuration
    }
}
