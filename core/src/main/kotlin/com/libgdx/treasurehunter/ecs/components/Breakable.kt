package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.tiled.ItemEntry

data class Breakable(
    var itemsInside : List<ItemEntry> = listOf(),
    var hitCount : Int,
    var damageTaken : Int = 0
) : Component <Breakable> {

    override fun type() = Breakable

    companion object : ComponentType<Breakable>()

}
