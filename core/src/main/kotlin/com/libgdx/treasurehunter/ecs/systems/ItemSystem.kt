package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Fixture
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.ecs.components.Chest
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Inventory
import com.libgdx.treasurehunter.ecs.components.Item
import com.libgdx.treasurehunter.ecs.components.Key
import com.libgdx.treasurehunter.ecs.systems.DebugSystem.Companion.SCREEN_TOUCH_DEBUG_RECT
import com.libgdx.treasurehunter.ecs.systems.PhysicSystem.Companion.entity
import com.libgdx.treasurehunter.ecs.systems.PhysicSystem.Companion.isChest
import com.libgdx.treasurehunter.ecs.systems.PhysicSystem.Companion.isChestLocked
import com.libgdx.treasurehunter.event.Dialog
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.event.GameEventListener
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.ui.model.ButtonType
import ktx.box2d.query
import ktx.math.minus

class ItemSystem(
    private val gameCamera: OrthographicCamera = inject(),
    private val physicWorld: PhysicWorld = inject(),
) : IteratingSystem(
    family = family { all(Item) }
), GameEventListener {

    override fun onTickEntity(entity: Entity) {
        val itemComp = entity[Item]
        val itemType = itemComp.itemData.itemType
    }

    companion object {
        const val INTERACT_DIST = 1.25f
    }

    override fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.OnScreenTouchDownEvent -> handleScreenTouch(event)
            is GameEvent.OpenChestEvent -> handleChestOpening(event)
            else -> Unit
        }
    }

    private fun handleScreenTouch(event: GameEvent.OnScreenTouchDownEvent) {
        val worldCoords =
            gameCamera.unproject(Vector3(event.screenX.toFloat(), event.screenY.toFloat(), 0f))
        SCREEN_TOUCH_DEBUG_RECT.set(worldCoords.x, worldCoords.y, 0.12f, 0.12f)
        physicWorld.query(
            SCREEN_TOUCH_DEBUG_RECT.x,
            SCREEN_TOUCH_DEBUG_RECT.y,
            SCREEN_TOUCH_DEBUG_RECT.x + SCREEN_TOUCH_DEBUG_RECT.width,
            SCREEN_TOUCH_DEBUG_RECT.y + SCREEN_TOUCH_DEBUG_RECT.height
        ) { fixture ->
            when {
                checkDistanceBetweenEntities(fixture) -> true
                fixture.isChest -> handleChestClick(fixture)
                fixture.isChestLocked -> handleLockedChestClicked(fixture)
                else -> true
            }
        }
    }

    private fun handleChestClick(fixture: Fixture): Boolean {
        GameEventDispatcher.fireEvent(
            GameEvent.OpenChestEvent(
                fixture.entity!!
            )
        )
        return false
    }

    private fun handleLockedChestClicked(fixture: Fixture): Boolean {
        val chest = fixture.entity?.getOrNull(Chest) ?: return true
        if (!chest.isLocked) {
            GameEventDispatcher.fireEvent(
                GameEvent.OpenChestEvent(
                    fixture.entity!!
                )
            )
            return false
        }
        val player = world.family { all(EntityTag.PLAYER) }.firstOrNull() ?: return true
        val inventory = player.getOrNull(Inventory) ?: return true
        val chestEntity = fixture.entity ?: return true
        if (inventory.hasItem(Key::class)) {
            GameEventDispatcher.fireEvent(
                GameEvent.ChestClickedEvent(
                    Dialog.OPEN_CHEST.dialogText,
                    chestEntity,
                    listOf(ButtonType.YES, ButtonType.NO)
                )
            )
        }else{
            GameEventDispatcher.fireEvent(
                GameEvent.ChestClickedEvent(
                    Dialog.OPEN_CHEST_LOCKED.dialogText,
                    chestEntity,
                    listOf(ButtonType.OK)
                )
            )
        }
        return false
    }

    private fun handleChestOpening(event: GameEvent.OpenChestEvent) {
        val chest = event.chestEntity[Chest]
        if (chest.isLocked){
            val player = world.family { all(EntityTag.PLAYER) }.firstOrNull() ?: return
            val inventory = player.getOrNull(Inventory) ?: return
            if (inventory.hasItem(Key::class)) {
                inventory.removeItem(inventory.items.first { it.itemType is Key })
            } else {
                return
            }
        }
        chest.isOpened = !chest.isOpened
        chest.isLocked = false
    }

    private fun checkDistanceBetweenEntities(collisionFixture: Fixture): Boolean {
        val player = world.family { all(EntityTag.PLAYER) }.firstOrNull() ?: return true
        val playerPos = player[Graphic].center
        val entityPos = collisionFixture.entity?.get(Graphic)?.center ?: return true
        val diff = playerPos - entityPos
        return if (diff.len() > INTERACT_DIST) true else false
    }
}
