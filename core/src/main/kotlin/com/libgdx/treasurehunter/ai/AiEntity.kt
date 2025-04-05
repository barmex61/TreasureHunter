package com.libgdx.treasurehunter.ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.utils.animation
import com.libgdx.treasurehunter.ecs.components.State
import ktx.box2d.rayCast
import kotlin.math.abs
import kotlin.math.pow

data class AiEntity(
    val entity: Entity,
    val world: World,
    val physicWorld: PhysicWorld,
) {


    var currentAnimType = AnimationType.IDLE
    var frameDuration = 1f
    val animationType : AnimationType
        get() = with(world){
            entity[Animation].animationType
        }

    inline operator fun <reified T:Component<*>> get(type: ComponentType<T>) : T = with(world){
        return entity[type]
    }

    fun destroy() = with(world){
        entity.remove()
    }

    fun animation(animationType: AnimationType,playMode: PlayMode = PlayMode.LOOP) = world.animation(entity,animationType,playMode)

    fun state(state : EntityState){
        with(world){entity[State]}.stateMachine.changeState(state)
    }

    fun inRange(range: Float, targetEntity: Entity): Boolean = with(world){
        val (_,targetCenter) = targetEntity[Graphic]
        return@with inRange(targetCenter,range)
    }

    fun inRange(location:Vector2,tolerance:Float) : Boolean = with(world){
        val (_,center) = entity[Graphic]
        val diffX = (center.x - location.x)
        val diffY = (center.y - location.y)
        return@with diffX.pow(2) + diffY.pow(2) < tolerance.pow(2)
    }

    fun move(targetEntity: Entity) = with(world) {
        val (_,center) = entity[Graphic]
        val (_,targetCenter) = targetEntity[Graphic]

        val diffX = (center.x - targetCenter.x)
        val diffY = (center.y - targetCenter.y)
        entity[Move].direction = entity[Move].previousDirection
    }

    fun isAnimationDone(): Boolean = with(world){
        val animComp = entity[Animation]
        animComp.gdxAnimation!!.isAnimationFinished(animComp.timer)
    }


    fun isPathBlocked(targetEntity: Entity) : Boolean = with(world){
        val start = entity[Graphic].center
        val end = targetEntity[Graphic].center
        var blocked = false
        physicWorld.rayCast(start,end){fixture, point, normal, fraction ->
            if (fixture.body.type == BodyDef.BodyType.StaticBody && !fixture.isSensor){
                blocked = true
                return@rayCast 0f
            }
            return@rayCast -1f
        }

        return@with blocked
    }

    fun remove(){
        with(world){
            entity.remove()
        }
    }

}
