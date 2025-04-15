package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.Color
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.state.PlayerState
import com.libgdx.treasurehunter.ecs.components.Blink
import com.libgdx.treasurehunter.ecs.components.DamageTaken
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Flash
import com.libgdx.treasurehunter.ecs.components.Invulnarable
import com.libgdx.treasurehunter.ecs.components.Life
import com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.event.GameEvent.EntityLifeChangeEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher

class DamageSystem : IteratingSystem(family = family{all(Life,DamageTaken).none(Invulnarable,Blink)}) {

    override fun onTickEntity(entity: Entity) {
        val damageTaken = entity[DamageTaken]
        val lifeComp = entity[Life]
        val (damageAmount) = damageTaken
        lifeComp.currentLife = (lifeComp.currentLife - damageAmount).coerceAtLeast(0)
        GameEventDispatcher.fireEvent(EntityLifeChangeEvent(lifeComp.currentLife))
        if (entity has EntityTag.PLAYER){
            entity.configure {
                it += Invulnarable(1f)
                it += Blink(1f,0.075f)
                it += Flash(color = Color.RED, flashAmount = 2, flashDuration = 0.08f, flashInterval = 0.15f)
            }
        }

    }
}
