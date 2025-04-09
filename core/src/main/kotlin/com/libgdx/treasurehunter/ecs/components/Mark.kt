package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World

data class Mark(
    var markDuration : Float,
    val markOffset : Vector2,
    val markEntity : Entity
) : Component <Mark> {

    override fun type() = Mark
    override fun World.onRemove(entity: Entity) {
        markEntity.configure {
            it -= EntityTag.HAS_MARK
        }
    }
    companion object : ComponentType<Mark>()

}
