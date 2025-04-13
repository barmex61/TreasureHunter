package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.utils.plus
import ktx.math.vec2

data class Physic(
    val body : Body,
    val previousPosition : Vector2 = vec2()
    ) : Component <Physic> {

    val onAir : Boolean
        get() {
            return body.linearVelocity.y !in (-0.1f..0.1f)
        }

    override fun type() = Physic

    override fun World.onRemove(entity: Entity) {
        body.userData = null
        body.world.destroyBody(body)
    }

    companion object : ComponentType<Physic>()

}
