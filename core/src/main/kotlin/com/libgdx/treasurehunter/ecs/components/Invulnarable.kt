package com.libgdx.treasurehunter.ecs.components


import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import kotlin.experimental.or

data class Invulnarable(var time : Float) : Component <Invulnarable> {

    override fun type() = Invulnarable


    companion object : ComponentType<Invulnarable>()

}
