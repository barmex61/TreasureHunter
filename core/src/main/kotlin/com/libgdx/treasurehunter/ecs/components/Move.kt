package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

enum class MoveDirection(val valueX : Int){
    LEFT(-1),
    RIGHT(1),
    NONE(0),
    UP(1),
    DOWN(-1);

    fun opposite() : MoveDirection {
        return when(this){
            LEFT -> RIGHT
            RIGHT -> LEFT
            UP -> DOWN
            DOWN -> UP
            NONE -> NONE
        }
    }

    fun isLeftOrDown() = this == DOWN || this == LEFT
    fun isRightOrUp() = this == UP || this == RIGHT
    fun isRightOrLeftOrNone() = this == RIGHT || this == LEFT || this == NONE
    fun isNone() = this == NONE
    fun isUpOrDown() = this == UP || this == DOWN

    companion object{
        fun horizontalValueOf(value : Int): MoveDirection {
            return when(value){
                1 -> RIGHT
                -1 -> LEFT
                else -> NONE
            }
        }
    }
}

data class Move(var flipX : Boolean = false,
                var direction : MoveDirection = MoveDirection.NONE,
                var currentSpeed : Float = 0f,
                var maxSpeed : Float,
                var timer : Float = 0f,
                var timeToMax : Float,
                var previousDirection : MoveDirection = MoveDirection.NONE,
                val defaultMaxSpeed : Float = maxSpeed,
                var maxReduceTimer : Float = 0f,
                var stop : Boolean = false
) : Component <Move> {

    override fun type() = Move

    companion object : ComponentType<Move>()

}
