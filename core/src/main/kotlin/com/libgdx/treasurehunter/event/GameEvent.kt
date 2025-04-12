package com.libgdx.treasurehunter.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.enums.ParticleType

sealed class GameEvent {
    data class MapChangeEvent(val tiledMap : TiledMap) : GameEvent()
    data class EntityLifeChangeEvent(val entityLife : Int) : GameEvent()
    data class CollectableItemEvent(val collectableEntity : Entity,val playerEntity : Entity) : GameEvent()
    data class RemoveEntityEvent(val entity : Entity) : GameEvent()
    data class ParticleEvent(val owner : Entity,val particlePosition : Vector2,val particleType : ParticleType) : GameEvent()
    object AttackStartEvent : GameEvent()
}

interface GameEventListener {
    fun onEvent(event : GameEvent)
}

object GameEventDispatcher{
    private val gameEventListeners = mutableSetOf<GameEventListener>()

    fun registerListener(gameEventListener: GameEventListener){
        gameEventListeners += gameEventListener
    }

    fun unRegisterListener(gameEventListener: GameEventListener){
        gameEventListeners -= gameEventListener
    }

    fun fireEvent(gameEvent: GameEvent){
        gameEventListeners.forEach { gameEventListener ->
            gameEventListener.onEvent(gameEvent)
        }
    }
}
