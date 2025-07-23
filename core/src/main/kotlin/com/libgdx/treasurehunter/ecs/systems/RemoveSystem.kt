package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Remove

class RemoveSystem : IteratingSystem(
    family = family{any(Remove, EntityTag.COLLECTED).none(EntityTag.RESPAWNABLE)}
) {
    override fun onTickEntity(entity: Entity) {
        if (entity has Remove){
            val remove = entity[Remove]
            remove.removeTimer -= deltaTime
            if (remove.instantRemove || remove.removeTimer <= 0f){
                entity.remove()
            }
        }
        if (entity has EntityTag.COLLECTED){
            entity.remove()
        }
    }
}
