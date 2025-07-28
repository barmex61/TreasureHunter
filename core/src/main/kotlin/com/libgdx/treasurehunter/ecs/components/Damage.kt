package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

data class Damage(val damage : Int,val isCrit : Boolean,val sourceEntity : Entity, val isContinuous : Boolean ) : Component <Damage> {

    override fun type() = Damage

    companion object : ComponentType<Damage>()

}
