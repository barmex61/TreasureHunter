package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.utils.GameObject

data class Collectable(val gameObject: GameObject) : Component <Collectable> {

    override fun type() = Collectable

    companion object : ComponentType<Collectable>()

}
