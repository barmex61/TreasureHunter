package com.libgdx.treasurehunter.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.enums.ParticleType
import com.libgdx.treasurehunter.enums.SoundAsset

sealed class GameEvent {
    data class MapChangeEvent(val tiledMap : TiledMap) : GameEvent()
    data class EntityLifeChangeEvent(val entityLife : Int,val maxLife : Int,val entity : Entity) : GameEvent()
    data class CollectableItemEvent(val collectableEntity : Entity,val playerEntity : Entity) : GameEvent()
    data class ParticleEvent(val owner : Entity,val particleType : ParticleType) : GameEvent()
    data class AudioChangeEvent(val soundVolume : Float,val musicVolume : Float,val muteMusic: Boolean,val muteSound : Boolean) : GameEvent()
    data class PlaySoundEvent(val soundAsset: SoundAsset) : GameEvent()
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
