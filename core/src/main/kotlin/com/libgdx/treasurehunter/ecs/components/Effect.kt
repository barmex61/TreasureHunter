package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World

data class Effect(
    var effectDuration : Float,
    val effectOffset : Vector2,
    val startPosition : Vector2,
    val owner : Entity,
    val stickToOwner : Boolean = false
) : Component <Effect> {

    override fun type() = Effect
    companion object : ComponentType<Effect>()

}
