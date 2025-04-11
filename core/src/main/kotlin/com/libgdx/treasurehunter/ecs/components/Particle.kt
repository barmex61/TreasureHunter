package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

data class Particle(
    val sprite : Sprite,
    val offset : Vector2,
    val owner : Entity
) : Component <Particle> {

    override fun type() = Particle

    companion object : ComponentType<Particle>()

}
