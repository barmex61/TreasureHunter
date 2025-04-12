package com.libgdx.treasurehunter.ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ecs.components.AiComponent
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.DamageTaken
import com.libgdx.treasurehunter.ecs.components.Jump
import com.libgdx.treasurehunter.ecs.components.Life
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.MoveDirection
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.utils.animation
import com.libgdx.treasurehunter.utils.distance
import kotlin.math.pow
import kotlin.math.sqrt

data class CrewEntity(
    val world: World,
    val entity: Entity
){

    val isGetHit: Boolean
        get() = getOrNull(DamageTaken) != null

    val isDead: Boolean
        get() {
            val lifeComp = getOrNull(Life) ?: return true
            return lifeComp.isDead
        }

    val isEnemyNearby : Boolean
        get() = this[AiComponent].nearbyEntities.isNotEmpty()

    val canAttack : Boolean
        get() {
            val attackComp = getOrNull(Attack) ?: return false
            return attackComp.doAttack
        }

    var stop : Boolean
        get() {
            val moveComp = getOrNull(Move) ?: return false
            return moveComp.stop
        }
        set(value) {
            val moveComp = getOrNull(Move) ?: return
            moveComp.stop = value
        }

    val position : Vector2
        get() = this[Physic].body.position

    val animationDone : Boolean
        get() {
            val mainAnimationData = getOrNull(Animation)?.animationData ?: return false
            return mainAnimationData.gdxAnimation!!.isAnimationFinished(mainAnimationData.timer)
        }

    val moveCurrent : Float
        get() {
            val moveComp = getOrNull(Move) ?: return 0f
            return moveComp.currentSpeed
        }

    val cantMoveForward : Boolean
        get() {
            val physicComp = getOrNull(Physic) ?: return false
            val moveComp = getOrNull(Move) ?: return false
            return (physicComp.body.linearVelocity.x.compareTo(0f) == 0 && moveComp.currentSpeed != 0f)
        }

    inline operator fun <reified T:Component<*>> get(type: ComponentType<T>) : T = with(world){
        return entity[type]
    }

    inline fun <reified T: Component<*>> getOrNull(type: ComponentType<T>) : T? = with(world){
        return entity.getOrNull(type)
    }

    fun animation(animationType: AnimationType,playMode: PlayMode = PlayMode.LOOP,frameDuration: Float? = null) = with(world){
        animation(entity,animationType,playMode,frameDuration)
    }

    fun moveTo(targetPosition : Vector2){
        val moveComponent = getOrNull(Move) ?: return
        moveComponent.direction = MoveDirection.horizontalValueOf(targetPosition.x.compareTo(position.x).toInt())
    }

    fun inRange(targetPosition: Vector2) :Boolean {
        val diff = distance(targetPosition,position)
        val aiComponent = getOrNull(AiComponent) ?: return false
        return aiComponent.nearbyEntities.isNotEmpty() || diff <= 1f
    }

    fun jump(){
        val jumpComponent = getOrNull(Jump) ?: return
        jumpComponent.wantsJump = true
    }




}
