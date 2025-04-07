package com.libgdx.treasurehunter.event

import com.badlogic.gdx.maps.tiled.TiledMap

sealed class GameEvent {
    data class MapChangeEvent(val tiledMap : TiledMap) : GameEvent()
    data class EntityLifeChangeEvent(val entityLife : Int) : GameEvent()
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
