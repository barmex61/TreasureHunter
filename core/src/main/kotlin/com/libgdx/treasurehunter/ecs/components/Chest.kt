package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.utils.GameObject

data class Chest(
    val gameObject: GameObject,
    var isOpened: Boolean = false,
    var isLocked : Boolean = false,
    var isUnlockAnimPlayed : Boolean = false
) : Component <Chest> {

    override fun type() = Chest

    companion object : ComponentType<Chest>()

}
