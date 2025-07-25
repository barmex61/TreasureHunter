package com.libgdx.treasurehunter.ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityUpdateContext
import com.github.quillraven.fleks.UniqueId
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ecs.components.AiComponent
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.AttackType
import com.libgdx.treasurehunter.ecs.components.DamageTaken
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Invulnarable
import com.libgdx.treasurehunter.ecs.components.Jump
import com.libgdx.treasurehunter.ecs.components.Life
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.MoveDirection
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.components.Remove
import com.libgdx.treasurehunter.ecs.systems.DebugSystem.Companion.RAY_CAST_POLYLINE
import com.libgdx.treasurehunter.ecs.systems.PhysicSystem.Companion.isBodyFixture
import com.libgdx.treasurehunter.game.PhysicWorld

import com.libgdx.treasurehunter.state.EntityState
import com.libgdx.treasurehunter.utils.animation
import com.libgdx.treasurehunter.utils.distance
import ktx.math.component1
import ktx.math.component2
import ktx.math.minus
import ktx.math.vec2

data class CrewEntity(
    val world: World,
    val entity: Entity,
    val physicWorld: PhysicWorld,
) {

    val isGetHit: Boolean
        get() = entity.getOrNull(DamageTaken) != null

    val isDead: Boolean
        get() {
            val lifeComp = entity.getOrNull(Life) ?: return true
            return lifeComp.isDead
        }

    val isJumping: Boolean
        get() {
            val (_, linY) = get(Physic).body.linearVelocity
            return linY > EntityState.Companion.TOLERANCE_Y
        }

    val isFalling: Boolean
        get() {
            val (_, linY) = get(Physic).body.linearVelocity
            return linY < -EntityState.Companion.TOLERANCE_Y
        }

    val isEnemyNearby: Boolean
        get() {
            return get(AiComponent).nearbyEntities.isNotEmpty()
        }

    val isDynamicEntity: Boolean
        get() {
            return entity.getOrNull(Jump) != null && entity.getOrNull(Move) != null
        }


    var doAttack: Boolean
        get() {
            val attackComp = entity.getOrNull(Attack) ?: return false
            val doAttack = attackComp.doAttack
            return doAttack
        }
        set(value) {
            val attackComp = entity.getOrNull(Attack) ?: return
            attackComp.doAttack = value
        }


    var stop: Boolean
        get() {
            val moveComp = entity.getOrNull(Move) ?: return false
            return moveComp.stop
        }
        set(value) {
            val moveComp = entity.getOrNull(Move) ?: return
            moveComp.stop = value
        }

    val position: Vector2
        get() = this[Graphic].center

    val attackRange: Float
        get() = this[Attack].attackMetaData.attackRange

    val invulnarable: Boolean
        get() = entity.getOrNull(Invulnarable) != null

    val animationType: AnimationType
        get() = this[Animation].animationData.animationType

    val animationDone: Boolean
        get() {
            val mainAnimationData = entity.getOrNull(Animation)?.animationData ?: return false
            return mainAnimationData.gdxAnimation!!.isAnimationFinished(mainAnimationData.timer)
        }

    private var directionChangeTimer: Float = 0f
    private val directionChangeInterval: Float = 0.2f

    val aiWanderRadius: Float
        get() = get(AiComponent).aiWanderRadius

    val playerPosition: Vector2
        get() {
            val playerEntity = get(AiComponent).nearbyEntities.first()
            return playerEntity[Graphic].center
        }

    val attackType: AttackType
        get() {
            return get(Attack).attackMetaData.attackType
        }

    val attackAnimPlayMode: PlayMode
        get() {
            return get(Attack).attackMetaData.attackAnimPlayMode
        }

    inline operator fun <reified T : Component<*>> get(type: ComponentType<T>): T = with(world) {
        return entity[type]
    }

    inline fun <reified T : Component<*>> Entity.getOrNull(type: ComponentType<T>): T? = with(world) {
        return entity.getOrNull(type)
    }

    inline operator fun <reified T : Component<*>> Entity.get(type: ComponentType<T>): T =
        with(world) {
            return this@get[type]
        }

    infix fun Entity.hasNo(type: UniqueId<*>): Boolean = with(world) {
        return@with this@hasNo.hasNo(type)
    }

    infix fun Entity.has(type: UniqueId<*>): Boolean = with(world) {
        return@with this@has.has(type)
    }

    inline fun Entity.configure(configuration: EntityUpdateContext.(Entity) -> Unit) = with(world) {
        this@configure.configure {
            configuration(it)
        }
    }

    fun removeDamageTaken() {
        val damageTaken = entity.getOrNull(DamageTaken) ?: return
        if (damageTaken.isContinuous) return
        entity.configure {
            it -= DamageTaken
        }
    }

    fun animation(
        animationType: AnimationType,
        playMode: PlayMode = PlayMode.LOOP,
        frameDuration: Float? = null,
    ) = with(world) {
        animation(entity, animationType, playMode, frameDuration)
    }

    fun moveTo(targetPosition: Vector2) {
        val moveComponent = entity.getOrNull(Move) ?: return
        if (directionChangeTimer == 0f || directionChangeTimer >= directionChangeInterval) {
            moveComponent.direction =
                MoveDirection.horizontalValueOf(targetPosition.x.compareTo(position.x).toInt())
            directionChangeTimer = 0.1f
        }
        directionChangeTimer += world.deltaTime

    }

    fun inRange(targetPosition: Vector2): Boolean {
        val diff = distance(targetPosition, position)
        val inRange = diff <= attackRange
        if (inRange) {
            entity.getOrNull(Attack)?.let { attack ->
                attack.wantsToAttack = true
            }
        }
        return inRange
    }

    fun jump() {
        val jumpComponent = entity.getOrNull(Jump) ?: return
        jumpComponent.wantsJump = true
    }

    fun raycast(
        rayCastLength: Vector2,
        shouldFlip: Boolean = true,
        collideCallback: (isCollidedWithWall: Boolean, diffY: Float) -> Unit,
    ) {
        val graphic = entity[Graphic]
        val move = entity[Move]
        val center = graphic.center
        val bottomCenter = graphic.bottomCenter
        val startPosition = vec2(center.x, center.y - (center.y - bottomCenter.y)/2f)
        val flipX = move.flipX
        val mirroredRayCast = if (shouldFlip) {
            if (flipX) rayCastLength else vec2(rayCastLength.x * -1f, rayCastLength.y)
        } else rayCastLength
        var isCollidedWithWall = false
        RAY_CAST_POLYLINE.vertices = floatArrayOf(
            startPosition.x, startPosition.y,
            startPosition.x + mirroredRayCast.x, startPosition.y + mirroredRayCast.y
        )
        physicWorld.rayCast(
            { fixture, vector1, vector2, value ->
                if (!fixture.isSensor && !fixture.isBodyFixture) {
                    isCollidedWithWall = true
                }
                return@rayCast -1f
            },
            startPosition.x,
            startPosition.y,
            startPosition.x + mirroredRayCast.x,
            startPosition.y + mirroredRayCast.y
        )
        collideCallback(isCollidedWithWall, mirroredRayCast.y)
    }

    fun remove() {
        if (entity hasNo Remove) {

            entity.configure {
                it += Remove(
                    instantRemove = false,
                    removeTimer = 0.5f
                )
            }
        }

    }


}
