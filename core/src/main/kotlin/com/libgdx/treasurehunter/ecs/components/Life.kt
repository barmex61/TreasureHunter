package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.event.GameEvent.EntityLifeChangeEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher.fireEvent

data class Life(
    val maxLife : Int,
    var damageTaken : Int = 0,
    var isDead : Boolean = false,
    val owner : Entity
) : Component <Life> {

    var currentLife : Int = maxLife
        set(value) {
            field = value
            fireEvent(EntityLifeChangeEvent(currentLife,maxLife,owner))
        }
    override fun type() = Life

    companion object : ComponentType<Life>()

}
