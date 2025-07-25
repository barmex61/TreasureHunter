package com.libgdx.treasurehunter.ui.model

import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.Flow

data class EntityLife(
    val currentLife : Int,
    val maxLife : Int
)

class GameModel(
    world: World
): GameEventListener{
    private val playerEntities = world.family { all(EntityTag.PLAYER) }
    val playerLife  = MutableStateFlow(EntityLife(0,0))
    override fun onEvent(event: GameEvent) {
        when(event){
            is GameEvent.EntityLifeChangeEvent->{
                if (event.entity in playerEntities){
                    playerLife.value = playerLife.value.copy(currentLife = event.entityLife, maxLife = event.maxLife)
                }else{
                   // enemyLifeBarScale.value = event.entityLife / event.maxLife.toFloat()
                }
            }

            else -> Unit
        }
    }
}
