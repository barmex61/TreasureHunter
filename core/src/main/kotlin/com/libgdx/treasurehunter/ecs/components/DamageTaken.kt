package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType


data class DamageTaken(var damageAmount : Int = 0,val isContinuous : Boolean) : Component <DamageTaken> {

    override fun type() = DamageTaken

    companion object : ComponentType<DamageTaken>()

}
