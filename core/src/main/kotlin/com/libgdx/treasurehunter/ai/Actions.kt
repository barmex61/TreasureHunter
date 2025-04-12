package com.libgdx.treasurehunter.ai

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.libgdx.treasurehunter.ecs.components.AnimationType
import ktx.math.random

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
            entity.stop = false
            return Status.SUCCEEDED
        }
        return super.execute()
    }
}



