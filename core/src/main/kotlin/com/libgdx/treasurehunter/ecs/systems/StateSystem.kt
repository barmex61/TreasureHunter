package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.State

class StateSystem : IteratingSystem(family = family { all(State) }) {

    override fun onTickEntity(entity: Entity) {
        entity[State].stateMachine.update()
    }
}
