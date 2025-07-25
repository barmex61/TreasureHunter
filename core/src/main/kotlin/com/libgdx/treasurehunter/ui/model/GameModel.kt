package com.libgdx.treasurehunter.ui.model

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.Flow

data class EntityLife(
    val entity: Entity,
    val currentLife : Int,
    val maxLife : Int,
    val entityIcon : String
)

class GameModel: GameEventListener{
    val playerLife  = MutableStateFlow(EntityLife(Entity.NONE,0,0,""))
    val enemyLife = MutableStateFlow(EntityLife(Entity.NONE,0,0,""))
    override fun onEvent(event: GameEvent) {
        when(event){
            is GameEvent.EntityLifeChangeEvent->{
                if (event.isPlayer){
                    playerLife.value = playerLife.value.copy(entity = event.entity, currentLife = event.entityLife, maxLife = event.maxLife, entityIcon = event.entityIcon)
                }else{
                    enemyLife.value = enemyLife.value.copy(entity = event.entity, currentLife = event.entityLife, maxLife = event.maxLife,entityIcon = event.entityIcon)
                }
            }

            else -> Unit
        }
    }
}
