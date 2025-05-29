package com.libgdx.treasurehunter.ai

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.libgdx.treasurehunter.ecs.components.AttackType

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
class IsDynamicEntity : Conditions(){
    override fun condition(): Boolean = entity.isDynamicEntity
}
class IsStaticEntity : Conditions(){
    override fun condition(): Boolean = !entity.isDynamicEntity
}

class IsEnemyNearby : Conditions(){
    override fun condition(): Boolean = entity.isEnemyNearby
}
class IsEnemyNotNearby : Conditions(){
    override fun condition(): Boolean = !entity.isEnemyNearby
}

class CanAttack : Conditions(){
    override fun condition(): Boolean = entity.doAttack
}

class CanNotAttack : Conditions(){
    override fun condition(): Boolean = !entity.doAttack
}


class CanMove : Conditions(){
    override fun condition(): Boolean = !entity.stop
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
class IsFierceToothAttack : Conditions(){
    override fun condition() = entity.attackType == AttackType.FIERCE_TOOTH_ATTACK
}
class IsCrabbyAttack : Conditions(){
    override fun condition() = entity.attackType == AttackType.CRABBY_ATTACK
}
class IsPinkStarAttack : Conditions(){
    override fun condition() = entity.attackType == AttackType.PINK_STAR_ATTACK
}

