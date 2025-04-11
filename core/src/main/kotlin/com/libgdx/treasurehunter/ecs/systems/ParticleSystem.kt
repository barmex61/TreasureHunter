package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Particle
import com.libgdx.treasurehunter.ecs.components.Physic

class ParticleSystem : IteratingSystem(
    family = family{all(Particle)}
){

    override fun onTickEntity(entity: Entity) {
        val particle = entity[Particle]
        val physic = entity[Physic]
        particle.sprite.setPosition(physic.body.position.x,physic.body.position.y)
    }
}
