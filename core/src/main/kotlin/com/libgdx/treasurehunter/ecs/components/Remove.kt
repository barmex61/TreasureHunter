package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class Remove(
    val instantRemove : Boolean = true,
    var removeTimer : Float = 1f
) : Component <Remove> {

    override fun type() = Remove

    companion object : ComponentType<Remove>()

}
