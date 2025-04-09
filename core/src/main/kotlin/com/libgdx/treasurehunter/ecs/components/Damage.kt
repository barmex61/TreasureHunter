package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

data class Damage(val damage : Int,val sourceEntity : Entity) : Component <Damage> {

    override fun type() = Damage

    companion object : ComponentType<Damage>()

}
