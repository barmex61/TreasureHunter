package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Stat(
    val attack: Int,
    val defense: Int,
    val critChance : Float,
    val critDamage : Float,
    val resistance : Float
) : Component <Stat> {

    override fun type() = Stat

    companion object : ComponentType<Stat>()

}
