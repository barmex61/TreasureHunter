package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Particle
import com.libgdx.treasurehunter.ecs.components.Remove
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher



class ParticleSystem : IteratingSystem(
    family = family{all(Particle)}
) {
    override fun onTickEntity(entity: Entity) {
        val animComp = entity[Animation]
        val particle = entity[Particle]
        val animData = animComp.animationData
        if (animData.gdxAnimation?.isAnimationFinished(animData.timer) == true ){
            entity.configure {
                it += Remove()
            }
        }
    }
}
