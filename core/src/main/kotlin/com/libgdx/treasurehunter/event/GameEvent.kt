package com.libgdx.treasurehunter.event

import com.badlogic.gdx.maps.tiled.TiledMap
import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.ecs.components.ItemData
import com.libgdx.treasurehunter.enums.ParticleType
import com.libgdx.treasurehunter.enums.SoundAsset
import com.libgdx.treasurehunter.ui.model.ButtonType

sealed class GameEvent {
    data class MapChangeEvent(val tiledMap : TiledMap) : GameEvent()
    data class EntityLifeChangeEvent(val entityLife : Int,val maxLife : Int,val entity : Entity) : GameEvent()
    data class InventoryChangeEvent(val inventoryItems : List<ItemData>) : GameEvent()
    data class ParticleEvent(val owner : Entity,val particleType : ParticleType) : GameEvent()
    data class AudioChangeEvent(val soundVolume : Float,val musicVolume : Float,val muteMusic: Boolean,val muteSound : Boolean) : GameEvent()
    data class PlaySoundEvent(val soundAsset: SoundAsset) : GameEvent()
    data class EquippedItemChanged(val slotName: String, val item: ItemData?) : GameEvent()
    data class EquippedItemRemoved(val slotName: String, val entity: Entity) : GameEvent()
    data class EntityModelChangeEvent(val entity: Entity, val modelName: String) : GameEvent()
    data class EquipItemRequest(val slotName: String, val item: ItemData) : GameEvent()
    data class UnEquipItemRequest(val slotName: String, val item: ItemData) : GameEvent()
    data class OnScreenTouchDownEvent(val screenX : Int,val screenY : Int) : GameEvent()
    data class ChestClickedEvent(val dialogText: String,val chestEntity : Entity,val buttonTypes : List<ButtonType> ) : GameEvent()
    data class OpenChestEvent(val chestEntity: Entity) : GameEvent()
}

enum class Dialog(val dialogText : String){
    OPEN_CHEST("Would you like to open the chest?"),
    OPEN_CHEST_LOCKED("The chest is locked, you need a key to open it."),
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
