package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Damage(val damage : Int) : Component <Damage> {

    override fun type() = Damage

    companion object : ComponentType<Damage>()

}
