package com.libgdx.treasurehunter.ai

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.utils.distance
import ktx.math.random
import ktx.math.vec2
import kotlin.collections.remove

abstract class Actions : LeafTask<CrewEntity>(){

    val entity : CrewEntity
        get() = `object` as CrewEntity

    override fun copyTo(task: Task<CrewEntity>): Task<CrewEntity> = task
    override fun execute(): Status {
        return if (entity.isGetHit){
            Status.FAILED
        }else if(entity.isDead){
            Status.FAILED
        }else{
            Status.RUNNING
        }
    }
}

class Idle : Actions(){


    private var currentDuration : Float = (1f..2f).random()

    override fun execute(): Status {
        println("ACTİON IDLE")
        if (status != Status.RUNNING){
            entity.stop = true
            currentDuration = (1f..2f).random()
            entity.animation(AnimationType.IDLE)
            return Status.RUNNING
        }
        currentDuration -= GdxAI.getTimepiece().deltaTime
        if (entity.isEnemyNearby && entity.canAttack){
            entity.stop = false
            return Status.SUCCEEDED
        }
        if (currentDuration <= 0f){
            println("SUCCEDED")
            entity.stop = false
            return Status.SUCCEEDED
        }
        return super.execute()
    }
}

class Wander : Actions(){
    private val spawnPosition = vec2()
    private val targetPosition = vec2()
    private var wanderTimer = 1.5f
    override fun execute(): Status {

        if (status != Status.RUNNING){
            wanderTimer = 1.5f
            if (spawnPosition.isZero){
                spawnPosition.set(entity.position)
            }
            targetPosition.set(
                spawnPosition.x + (-5f..5f).random(),
                spawnPosition.y + (-5f..5f).random(),
            )
            entity.animation(AnimationType.RUN, frameDuration = 0.1f)
            entity.moveTo(targetPosition)
            return Status.RUNNING
        }
        if (entity.inRange(targetPosition)){
            return Status.SUCCEEDED
        }
        if (wanderTimer <= 0f){
            wanderTimer = 1.5f
            return Status.SUCCEEDED
        }
        if (entity.cantMoveForward){
            entity.jump()
        }

        wanderTimer -= GdxAI.getTimepiece().deltaTime
        return super.execute()
    }
}


class Hit : Actions(){
    override fun execute(): Status {
        println("ACTİON HIT")

        if (status != Status.RUNNING){
            entity.stop = true
            var frameDuration = 0.1f
            entity.animation(AnimationType.HIT,PlayMode.NORMAL, frameDuration)
            return Status.RUNNING
        }
        if (entity.animationDone){
            entity.stop = false
            return Status.SUCCEEDED
        }
        return Status.RUNNING
    }
}


class Dead : Actions(){
    override fun execute(): Status {

        if (status != Status.RUNNING){
            entity.stop = true
            entity.animation(AnimationType.DEAD_GROUND,PlayMode.NORMAL, 0.1F)
            return Status.RUNNING
        }
        if (entity.animationDone){
            GameEventDispatcher.fireEvent(GameEvent.RemoveEntityEvent(entity.entity))
            return Status.SUCCEEDED
        }
        return Status.RUNNING
    }
}



