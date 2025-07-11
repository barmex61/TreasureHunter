package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.EntityTag

class RemoveSystem : IteratingSystem(
    family = family{any(EntityTag.REMOVE, EntityTag.COLLECTED).none(EntityTag.RESPAWNABLE)}
) {
    override fun onTickEntity(entity: Entity) {
        if (entity has EntityTag.REMOVE) {
            println("entity has remove tag")
        }
        if (entity has EntityTag.COLLECTED) {
            println("entity has collected tag")
        }
        entity.remove()
    }
}
