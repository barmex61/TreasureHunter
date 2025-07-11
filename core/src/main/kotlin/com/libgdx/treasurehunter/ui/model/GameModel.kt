package com.libgdx.treasurehunter.ui.model

import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.Flow

class GameModel(
    world: World
): GameEventListener{
    private val playerEntities = world.family { all(EntityTag.PLAYER) }
    val playerLife  = MutableStateFlow<Int>(0)
    val playerLifeBarScale = MutableStateFlow<Float>(1f)
    val enemyLifeBarScale = MutableStateFlow<Float>(1f)
    override fun onEvent(event: GameEvent) {
        when(event){
            is GameEvent.EntityLifeChangeEvent->{
                if (event.entity in playerEntities){
                    playerLife.value = event.entityLife
                    playerLifeBarScale.value = event.entityLife / event.maxLife.toFloat()
                }else{
                    enemyLifeBarScale.value = event.entityLife / event.maxLife.toFloat()
                }
            }

            else -> Unit
        }
    }
}
