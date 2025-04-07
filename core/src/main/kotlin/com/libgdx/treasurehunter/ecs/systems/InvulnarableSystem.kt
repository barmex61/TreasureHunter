package com.libgdx.treasurehunter.ecs.systems


import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Invulnarable

class InvulnarableSystem : IteratingSystem(family = family { all(Invulnarable) }) {

    override fun onTickEntity(entity: Entity) {
        entity.getOrNull(Invulnarable)?.let { invComp->
            var (time) = invComp

            if (time <= 0f){
                entity.configure { it -= Invulnarable }
            }
            time -= deltaTime
            invComp.time = time
        }
    }
}
