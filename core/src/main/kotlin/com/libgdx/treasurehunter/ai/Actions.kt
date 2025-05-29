package com.libgdx.treasurehunter.ai

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import ktx.math.random
import ktx.math.vec2

abstract class Actions : LeafTask<CrewEntity>(){

    val entity : CrewEntity
        get() = `object` as CrewEntity

    override fun copyTo(task: Task<CrewEntity>): Task<CrewEntity> = task
    override fun execute(): Status {
        return if (entity.isGetHit || entity.isDead || entity.isJumping || entity.isFalling){
            Status.SUCCEEDED
        }else Status.RUNNING
    }
}

class Idle : Actions(){
    private var idleDuration : Float = (1f..3f).random()

    override fun execute(): Status {
        if (status != Status.RUNNING){
            entity.raycast(vec2(0f,-0.5f)) { isCollidedWithWall,diffY ->
                if (isCollidedWithWall && !entity.isJumping){
                    entity.stop = true
                }
            }

            idleDuration = (1f..3f).random()
            if (entity.animationType != AnimationType.IDLE){
                entity.animation(AnimationType.IDLE)
            }
            return Status.RUNNING
        }
        idleDuration -= GdxAI.getTimepiece().deltaTime
        if (entity.isEnemyNearby ){
            entity.stop = false
            return Status.SUCCEEDED
        }
        if (idleDuration <= 0f){
            entity.stop = false
            return Status.SUCCEEDED
        }

        return super.execute()
    }
}

class Wander: Actions(){
    @JvmField
    @TaskAttribute
    var moveToPlayer: Boolean = false
    private val spawnPosition = vec2()
    private val targetPosition = vec2()
    private var wanderTimer = 5f
    private var raycastInterval = 0.5f
    private var raycastTimer = 0f

    override fun execute(): Status {
        if (status != Status.RUNNING){
            raycastInterval = 0.5f
            raycastTimer = 0f
            wanderTimer = entity.aiWanderRadius * 0.5f
            entity.stop = false
            if (spawnPosition.isZero){
                spawnPosition.set(entity.position)
            }
            if (moveToPlayer){
                targetPosition.set(
                    entity.playerPosition.x,
                    entity.playerPosition.y,
                )
            }else{
                targetPosition.set(
                    spawnPosition.x + (-entity.aiWanderRadius..entity.aiWanderRadius).random(),
                    spawnPosition.y + (-entity.aiWanderRadius..entity.aiWanderRadius).random(),
                )
            }
            entity.animation(AnimationType.RUN, frameDuration = 0.1f)
            entity.moveTo(targetPosition)
            return Status.RUNNING
        }
        if (moveToPlayer){
            if (!entity.isEnemyNearby){
                return Status.FAILED
            }

            targetPosition.set(
                entity.playerPosition.x,
                entity.playerPosition.y,
            )
        }
        if (entity.inRange(targetPosition)){
            return Status.SUCCEEDED
        }
        if (wanderTimer <= 0f && !moveToPlayer){
            return Status.SUCCEEDED
        }
        if (raycastTimer >= raycastInterval){
            entity.raycast(vec2(0.7f,0f)){isCollidedWithWall,_ ->
                if (isCollidedWithWall){
                    entity.jump()
                }
            }
            raycastTimer = 0f
        }
        wanderTimer -= GdxAI.getTimepiece().deltaTime
        raycastTimer += GdxAI.getTimepiece().deltaTime
        return super.execute()
    }
    override fun copyTo(task: Task<CrewEntity>): Task<CrewEntity> {
        return (task as Wander).apply { task.moveToPlayer = this@Wander.moveToPlayer }
    }
}


class PinkStarAttack : Actions(){
    override fun execute(): Status {
        if (status != Status.RUNNING){
            entity.animation(entity.attackType.attackAnimType, entity.attackAnimPlayMode, 0.1f)
            return Status.RUNNING
        }
        if (!entity.doAttack){
            return Status.SUCCEEDED
        }
        return super.execute()
    }
}
class FierceToothAttack : Actions(){
    override fun execute(): Status {
        if (status != Status.RUNNING){
            entity.doAttack = false
            entity.animation(entity.attackType.attackAnimType, entity.attackAnimPlayMode, 0.1f)
            return Status.RUNNING
        }
        if (entity.animationDone){
            return Status.SUCCEEDED
        }
        return super.execute()
    }
}
class CrabbyAttack : Actions(){
    override fun execute(): Status {
        if (status != Status.RUNNING){
            entity.stop = true
            entity.doAttack = false
            entity.animation(entity.attackType.attackAnimType, entity.attackAnimPlayMode, 0.15f)
            return Status.RUNNING
        }
        if (entity.animationDone){
            entity.stop = false
            return Status.SUCCEEDED
        }
        return super.execute()
    }
}

class TotemIdle : Actions(){
    override fun execute(): Status {
        if (status != Status.RUNNING){
            println("totem idle")
            entity.animation(AnimationType.IDLE, PlayMode.NORMAL, 0.1f)
            return Status.RUNNING
        }
        if (entity.isEnemyNearby && entity.inRange(entity.playerPosition)){
           return Status.SUCCEEDED
        }

        return super.execute()
    }
}

class TotemAttack : Actions(){
    override fun execute(): Status {
        if (status != Status.RUNNING){
            entity.doAttack = false
            entity.animation(entity.attackType.attackAnimType, entity.attackAnimPlayMode, 0.1f)
            return Status.RUNNING
        }
        if (entity.animationDone){
            return Status.SUCCEEDED
        }
        return super.execute()
    }
}

class Jump : Actions(){
    override fun execute(): Status {
        if (status != Status.RUNNING){
            entity.animation(AnimationType.JUMP, PlayMode.NORMAL, 0.1f)
            return Status.RUNNING
        }
        if (!entity.isJumping){
            return Status.SUCCEEDED
        }
        return Status.RUNNING
    }
}

class Fall : Actions(){
    override fun execute(): Status {
        if (status != Status.RUNNING){
            entity.animation(AnimationType.FALL, PlayMode.NORMAL, 0.1f)
            return Status.RUNNING
        }
        if (!entity.isFalling){
            return Status.SUCCEEDED
        }
        return Status.RUNNING
    }
}


class Hit : Actions(){
    override fun execute(): Status {

        if (status != Status.RUNNING){
            entity.stop = true
            var frameDuration = 0.1f
            entity.removeDamageTaken()
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
            entity.remove()
            return Status.SUCCEEDED
        }
        return Status.RUNNING
    }
}



