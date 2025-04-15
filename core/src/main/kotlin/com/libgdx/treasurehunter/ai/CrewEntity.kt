package com.libgdx.treasurehunter.ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityUpdateContext
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ecs.components.AiComponent
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.AttackType
import com.libgdx.treasurehunter.ecs.components.DamageTaken
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Jump
import com.libgdx.treasurehunter.ecs.components.Life
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.MoveDirection
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.systems.DebugSystem.Companion.RAY_CAST_POLYLINE
import com.libgdx.treasurehunter.ecs.systems.PhysicSystem.Companion.isBodyFixture
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.state.EntityState
import com.libgdx.treasurehunter.utils.animation
import com.libgdx.treasurehunter.utils.distance
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2

data class CrewEntity(
    val world: World,
    val entity: Entity,
    val physicWorld: PhysicWorld
){

    val isGetHit: Boolean
        get() = getOrNull(DamageTaken) != null

    val isDead: Boolean
        get() {
            val lifeComp = getOrNull(Life) ?: return true
            return lifeComp.isDead
        }

    val isJumping : Boolean
        get() {
            val(_,linY) = get(Physic).body.linearVelocity
            return linY > EntityState.Companion.TOLERANCE_Y
        }

    val isFalling : Boolean
        get() {
            val(_,linY) = get(Physic).body.linearVelocity
            return linY < -EntityState.Companion.TOLERANCE_Y
        }

    val isEnemyNearby : Boolean
        get() = get(AiComponent).nearbyEntities.isNotEmpty()


    var doAttack : Boolean
        get() {
            val attackComp = getOrNull(Attack) ?: return false
            val doAttack = attackComp.doAttack
            return doAttack
        }
        set(value) {
            val attackComp = getOrNull(Attack) ?: return
            attackComp.doAttack = value
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
        get() = this[Graphic].center

    val attackRange: Float
        get() = this[Attack].attackMetaData.attackRange

    val animationType : AnimationType
        get() = this[Animation].animationData.animationType

    val animationDone : Boolean
        get() {
            val mainAnimationData = getOrNull(Animation)?.animationData ?: return false
            return mainAnimationData.gdxAnimation!!.isAnimationFinished(mainAnimationData.timer)
        }

    val aiWanderRadius : Float
        get() = get(AiComponent).aiWanderRadius

    val playerPosition : Vector2
        get() {
            val playerEntity = get(AiComponent).nearbyEntities.first()
            return playerEntity[Graphic].center
        }

    val attackType : AttackType
        get() {
            return get(Attack).attackMetaData.attackType
        }

    val attackAnimPlayMode : PlayMode
        get() {
            return get(Attack).attackMetaData.attackAnimPlayMode
        }

    inline operator fun <reified T:Component<*>> get(type: ComponentType<T>) : T = with(world){
        return entity[type]
    }

    inline fun <reified T: Component<*>> getOrNull(type: ComponentType<T>) : T? = with(world){
        return entity.getOrNull(type)
    }

    inline operator fun <reified T: Component<*>> Entity.get(type: ComponentType<T>): T = with(world) {
        return this@get[type]
    }

    inline fun Entity.configure(configuration : EntityUpdateContext.(Entity) -> Unit) = with(world) {
        this@configure.configure {
            configuration(it)
        }
    }

    fun removeDamageTaken(){
        val damageTaken = getOrNull(DamageTaken)?:return
        if (damageTaken.isContinuous) return
        entity.configure {
            it -= DamageTaken
        }
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
        val inRange = diff <= attackRange
        if (inRange){
            getOrNull(Attack)?.let { attack ->
                attack.wantsToAttack = true
            }
        }
        return inRange
    }

    fun jump(){
        val jumpComponent = getOrNull(Jump) ?: return
        jumpComponent.wantsJump = true
    }

    fun raycast(rayCastLength : Vector2) : Boolean{
        val centerPosition = get(Graphic).center
        val flipX = get(Move).flipX
        val mirroredRayCast = if (flipX) rayCastLength else vec2(rayCastLength.x * -1f ,rayCastLength.y)
        RAY_CAST_POLYLINE.vertices = floatArrayOf(centerPosition.x,centerPosition.y,centerPosition.x + mirroredRayCast.x,centerPosition.y + mirroredRayCast.y)
        var isCollidedWithWall = false
        physicWorld.rayCast({ fixture,vector1,vector2,value ->
            if (!fixture.isSensor && !fixture.isBodyFixture){
                isCollidedWithWall = true
            }
            return@rayCast -1f
        },centerPosition.x,centerPosition.y,centerPosition.x + mirroredRayCast.x,centerPosition.y + mirroredRayCast.y)
        return isCollidedWithWall
    }

    fun remove(){
        entity.configure {
            it += EntityTag.REMOVE
        }
    }

}
