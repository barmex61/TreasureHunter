package com.libgdx.treasurehunter.input

import com.badlogic.gdx.Input
import com.github.quillraven.fleks.World
import ktx.app.KtxInputAdapter
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Jump
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.MoveDirection

class KeyboardInputProcessor(world: World) : KtxInputAdapter {

    private var moveX = 0
    private var playerEntities = with(world) {
        family { all(EntityTag.PLAYER) }
    }
    var stopMovement : Boolean = false


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


    override fun keyDown(keycode: Int): Boolean {
        when(keycode){
            Input.Keys.D -> updatePlayerMovement(1)
            Input.Keys.A -> updatePlayerMovement(-1)
            Input.Keys.SPACE -> updatePlayerJump(true)
        }

        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        when(keycode){
            Input.Keys.D -> updatePlayerMovement(-1)
            Input.Keys.A -> updatePlayerMovement(1)
            Input.Keys.SPACE -> updatePlayerJump(false)
        }
        return false
    }
}
