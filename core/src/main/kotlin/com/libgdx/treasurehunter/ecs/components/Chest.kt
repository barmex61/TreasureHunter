package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.tiled.ItemEntry
import com.libgdx.treasurehunter.utils.GameObject

data class Chest(
    val gameObject: GameObject,
    var isOpened: Boolean = false,
    var isLocked : Boolean = false,
    var isUnlockAnimPlayed : Boolean = false,
    var itemsInside : List<ItemEntry> = emptyList(),
    var itemAppearInterval : Float = 0.5f,
) : Component <Chest> {

    var isItemsSpawned : Boolean = false

    override fun type() = Chest

    companion object : ComponentType<Chest>()

}
