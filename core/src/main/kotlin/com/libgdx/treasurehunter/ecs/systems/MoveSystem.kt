package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.math.Interpolation
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.MoveDirection
import com.libgdx.treasurehunter.ecs.components.Physic


class MoveSystem : IteratingSystem(
    family = family{all(Move, Physic)}
) {
    override fun onTickEntity(entity: Entity) {
        val moveComp = entity[Move]
        val physic = entity[Physic]
        var (isFlipX,direction,current,maxSpeed,timer,timeToMax,_,defaultMax,maxReduceTimer,stop,initialFlipX) = moveComp
        if (stop) {
            moveComp.currentSpeed = 0f
            return
        }

        if (direction != MoveDirection.NONE){
            moveComp.previousDirection = direction
            if ((current >0 && direction.isLeftOrDown() )|| (current<0 && direction.isRightOrUp())){
                timer = 0f
            }
            timer = (timer + (deltaTime* (1f/timeToMax))).coerceAtMost(1f)
            current = pow50outInterpolation.apply(1f,maxSpeed,timer)
            current *= direction.valueX
            var moveFlip = direction == MoveDirection.LEFT
            isFlipX = if (initialFlipX == false) moveFlip else !moveFlip

        }else{
            current = 0f
            timer = 0f
        }
        moveComp.currentSpeed = current
        moveComp.timer = timer
        moveComp.flipX = isFlipX
    }

    companion object{
        val pow50outInterpolation: Interpolation.PowOut = Interpolation.pow5Out
    }
}
