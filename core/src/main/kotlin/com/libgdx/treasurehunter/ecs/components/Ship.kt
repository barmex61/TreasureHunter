package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

data class Ship(
    var attachedEntities : MutableMap<Entity, Vector2> = mutableMapOf(),
    var isMoving : Boolean = false,
    var isWaveEffectEnabled : Boolean = true
) : Component <Ship> {

    override fun type() = Ship

    companion object : ComponentType<Ship>()

}
