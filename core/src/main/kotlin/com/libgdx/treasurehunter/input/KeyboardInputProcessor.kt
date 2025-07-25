package com.libgdx.treasurehunter.input

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector3
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.AttackState
import com.libgdx.treasurehunter.ecs.components.AttackType
import ktx.app.KtxInputAdapter
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Jump
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.MoveDirection
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.systems.CameraMovement
import com.libgdx.treasurehunter.ecs.systems.CameraSystem
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.ui.navigation.StageNavigator
import com.libgdx.treasurehunter.ui.navigation.TransitionType
import com.libgdx.treasurehunter.ui.navigation.ViewType
import ktx.math.vec2

class KeyboardInputProcessor(val world: World,val toggleInventory : () -> Unit) : KtxInputAdapter {


    private var moveX = 0
    private var playerEntities = with(world) {
        family { all(EntityTag.PLAYER) }
    }

    var wantsAttack : Boolean = false
    var cameraDirection : CameraMovement = CameraMovement(0,0)


    fun addCameraMovement(moveDirection: MoveDirection){
        cameraDirection += moveDirection
        world.system<CameraSystem>().cameraMovement = cameraDirection
    }

    fun removeCameraMovement(moveDirection: MoveDirection){
        cameraDirection -= moveDirection
        world.system<CameraSystem>().cameraMovement = cameraDirection
    }

    fun updateCameraZoom(zoomValue : Float){
        world.system<CameraSystem>().zoom = zoomValue
    }

    fun updatePlayerMovement(moveValue : Int){

        if(moveX == moveValue) return
        moveX = (moveX + moveValue).coerceIn(-1,1)
        playerEntities.forEach {
            it.getOrNull(Move)?.let {
               it.direction = MoveDirection.horizontalValueOf(moveX)
            }
        }
    }


    fun updatePlayerJump(jump : Boolean){

        playerEntities.forEach { it[Jump].wantsJump = jump}
    }

    private fun updatePlayerAttack(attackType: AttackType){
        playerEntities.forEach { playerEntity ->
            val attackComp = playerEntity.getOrNull(Attack)?:return@forEach
            attackComp.queuedAttackType = attackType
            attackComp.wantsToAttack = true
        }
    }



    override fun keyDown(keycode: Int): Boolean {
        when(keycode){
            Input.Keys.D -> updatePlayerMovement(1)
            Input.Keys.A -> updatePlayerMovement(-1)
            Input.Keys.SPACE -> updatePlayerJump(true)
            Input.Keys.NUMPAD_3 -> updatePlayerAttack(AttackType.ATTACK_3)
            Input.Keys.NUMPAD_2 -> updatePlayerAttack(AttackType.ATTACK_2)
            Input.Keys.NUMPAD_1 -> updatePlayerAttack(AttackType.ATTACK_1)
            Input.Keys.LEFT -> addCameraMovement(MoveDirection.LEFT)
            Input.Keys.RIGHT -> addCameraMovement(MoveDirection.RIGHT)
            Input.Keys.UP -> addCameraMovement(MoveDirection.UP)
            Input.Keys.DOWN -> addCameraMovement(MoveDirection.DOWN)
            Input.Keys.NUMPAD_8 -> updateCameraZoom(0.1f)
            Input.Keys.NUMPAD_5 -> updateCameraZoom(-0.1f)
            Input.Keys.P -> updatePlayerAttack(AttackType.THROW)
            Input.Keys.I -> toggleInventory()
        }

        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        when(keycode){
            Input.Keys.D -> updatePlayerMovement(-1)
            Input.Keys.A -> updatePlayerMovement(1)
            Input.Keys.SPACE -> updatePlayerJump(false)
            Input.Keys.LEFT -> removeCameraMovement(MoveDirection.LEFT)
            Input.Keys.RIGHT -> removeCameraMovement(MoveDirection.RIGHT)
            Input.Keys.UP -> removeCameraMovement(MoveDirection.UP)
            Input.Keys.DOWN -> removeCameraMovement(MoveDirection.DOWN)
            Input.Keys.NUMPAD_8 -> updateCameraZoom(0f)
            Input.Keys.NUMPAD_5 -> updateCameraZoom(0f)
        }
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        GameEventDispatcher.fireEvent(GameEvent.OnScreenTouchDownEvent(screenX,screenY))
        return false
    }
}
