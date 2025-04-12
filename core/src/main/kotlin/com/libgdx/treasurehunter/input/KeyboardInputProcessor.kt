package com.libgdx.treasurehunter.input

import com.badlogic.gdx.Input
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

class KeyboardInputProcessor(val world: World) : KtxInputAdapter {

    private var moveX = 0
    private var playerEntities = with(world) {
        family { all(EntityTag.PLAYER) }
    }
    var stopMovement : Boolean = false
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

    fun updatePlayerMovement(moveValue : Int,reset : Boolean = false){
        if (stopMovement) {
            stopMovement()
            return
        }
        if ((reset && moveX == 0) || moveX == moveValue) return
        moveX = (moveX + moveValue).coerceIn(-1,1)
        if (reset) moveX = 0
        playerEntities.forEach {
            it.getOrNull(Move)?.let {
                it.direction = MoveDirection.horizontalValueOf(moveX)
            }
        }
    }

    private fun stopMovement(){
        moveX = 0
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
            if (attackComp.attackState == AttackState.READY){
                val onAir = playerEntity[Physic].body.linearVelocity.y !in (-0.1f..0.1f)
                val updatedAttackType = when{
                    attackType == AttackType.THROW -> AttackType.THROW
                    !onAir -> attackType
                    attackType == AttackType.ATTACK_1 && onAir -> AttackType.AIR_ATTACK_1
                    attackType == AttackType.ATTACK_2 && onAir -> AttackType.AIR_ATTACK_2
                    else -> return@forEach
                }
                attackComp.wantsToAttack = true
                attackComp.attackItem.attackType = updatedAttackType
            }
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
        }

        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        when(keycode){
            Input.Keys.D -> updatePlayerMovement(-1)
            Input.Keys.A -> updatePlayerMovement(1)
            Input.Keys.SPACE -> updatePlayerJump(false)
            Input.Keys.NUMPAD_3 -> updatePlayerAttack(AttackType.ATTACK_3)
            Input.Keys.NUMPAD_2 -> updatePlayerAttack(AttackType.ATTACK_2)
            Input.Keys.NUMPAD_1 -> updatePlayerAttack(AttackType.ATTACK_1)
            Input.Keys.LEFT -> removeCameraMovement(MoveDirection.LEFT)
            Input.Keys.RIGHT -> removeCameraMovement(MoveDirection.RIGHT)
            Input.Keys.UP -> removeCameraMovement(MoveDirection.UP)
            Input.Keys.DOWN -> removeCameraMovement(MoveDirection.DOWN)
            Input.Keys.NUMPAD_8 -> updateCameraZoom(0f)
            Input.Keys.NUMPAD_5 -> updateCameraZoom(0f)
            Input.Keys.P -> updatePlayerAttack(AttackType.THROW)
        }
        return false
    }
}
