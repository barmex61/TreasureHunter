package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World

data class Stat(
    val attack: Int,
    val defense: Int,
    val critChance : Float,
    val critDamage : Float,
    val resistance : Float,
    val jump : Float,
    val speed : Float,
    val life : Int
) : Component <Stat> {

    override fun type() = Stat

    companion object : ComponentType<Stat>()

}
