package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.enums.ParticleType
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher

data class Particle(
    val particleType : ParticleType,
    val owner : Entity
) : Component <Particle> {


    override fun type() = Particle

    companion object : ComponentType<Particle>()

}
