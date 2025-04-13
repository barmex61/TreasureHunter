package com.libgdx.treasurehunter.ai

import com.badlogic.gdx.ai.btree.Decorator
import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.Task.Status
import com.libgdx.treasurehunter.state.EntityState

abstract class Conditions : LeafTask<CrewEntity>(){
    val entity : CrewEntity
        get() = `object` as CrewEntity

    abstract fun condition() : Boolean

    override fun copyTo(task: Task<CrewEntity>): Task<CrewEntity> = task

    override fun execute(): Status {
        return if (condition()){
            Status.SUCCEEDED
        }else{
            Status.FAILED
        }
    }

}

class IsEnemyNearby : Conditions(){
    override fun condition(): Boolean = entity.isEnemyNearby
}

class CanAttack : Conditions(){
    override fun condition(): Boolean = entity.canAttack
}

class CanMove : Conditions(){
    override fun condition(): Boolean = !entity.stop
}
class InRange : Conditions(){
    override fun condition(): Boolean = entity.isEnemyNearby
}

class NotInRange : Conditions(){
    override fun condition(): Boolean = !entity.isEnemyNearby
}

class IsDead : Conditions(){
    override fun condition(): Boolean = entity.isDead
}
class IsGetHit : Conditions(){
    override fun condition() = entity.isGetHit
}
class IsJumping : Conditions(){
    override fun condition() = entity.isJumping
}
class IsFalling : Conditions(){
    override fun condition()= entity.isFalling
}

