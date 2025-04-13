package com.libgdx.treasurehunter.state

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityUpdateContext
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationData
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.AttackMeta
import com.libgdx.treasurehunter.ecs.components.Blink
import com.libgdx.treasurehunter.ecs.components.Collectable
import com.libgdx.treasurehunter.ecs.components.DamageTaken
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Mark
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.utils.animation
import com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.MarkType
import com.libgdx.treasurehunter.enums.ParticleType
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.tiled.sprite
import com.libgdx.treasurehunter.utils.GameObject
import kotlin.math.pow

data class StateEntity(
    val entity: Entity,
    val world: World,
    val physicWorld: PhysicWorld,
    val assetHelper: AssetHelper,
) {


    // ------ CAN BE REMOVED FOR NOW IT IS ONLY FOR PLAYER ENTITY ------
    val body : Body
        get() = this[Physic].body

    val doAttack : Boolean
        get() {
            var doAttack = this.getOrNull(Attack)?.doAttack?:return false
            return if (doAttack){
                this[Attack].doAttack = false
                true
            }else false
        }


    var runParticleTimer : Float = 0.5f


    // ------ CAN BE REMOVED -------

    inline operator fun <reified T:Component<*>> get(type: ComponentType<T>) : T = with(world){
        return entity[type]
    }

    inline fun <reified T: Component<*>> getOrNull(type: ComponentType<T>) : T? = with(world){
        return entity.getOrNull(type)
    }

    fun Entity.configure(configuration : EntityUpdateContext.(Entity) -> Unit) = with(world) { this@configure.configure(configuration) }

    fun fireParticleEvent(particleType: ParticleType) {
        GameEventDispatcher.fireEvent(GameEvent.ParticleEvent(entity, particleType))
    }

    fun animation(animationType: AnimationType,playMode: PlayMode = PlayMode.LOOP,frameDuration: Float? = null) = with(world){
        animation(entity,animationType,playMode,frameDuration)
    }

    fun state(state : EntityState,toPreviousState : Boolean = false){
        val stateComp = this[State]
        val nextState= if (toPreviousState) stateComp.stateMachine.previousState else state
        this[State].stateMachine.changeState(nextState)
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
        val mainAnimationData = entity[Animation].animationData
        mainAnimationData.gdxAnimation!!.isAnimationFinished(mainAnimationData.timer)
    }

    fun createMarkEntity(markType : MarkType) {
        entity.configure { aiEntity ->
            if (entity hasNo EntityTag.HAS_MARK){
                aiEntity += EntityTag.HAS_MARK
                val markPosition = entity[Graphic].center
                val gameObjectFromType = markType.toGameObject()
                world.entity{ markEntity ->
                    markEntity += Graphic(sprite(gameObjectFromType, AnimationType.IN,markPosition,assetHelper,0f))
                    markEntity += Animation(gameObjectFromType, AnimationData(
                        animationType = AnimationType.IN,
                        playMode = PlayMode.NORMAL,
                        frameDuration = 0.2f
                    ))
                    markEntity += Mark(1f,markType.offset,aiEntity)
                }
            }
        }
    }


    // ----------SWORD AI ENTITY---------------

    val isCollected : Boolean
        get() {
            with(world) {
                return this@StateEntity.entity hasNo EntityTag.COLLECTABLE
            }
        }

    var alpha : Float
        get() = this.getOrNull(Graphic)?.sprite?.color?.a?:1f
        set(value) {
            this.getOrNull(Graphic)?.sprite?.setAlpha(value)
        }

    var attackDestroyTimer : Float
        get() = this[AttackMeta].attackMetaData.attackDestroyTime
        set(value) {
            this[AttackMeta].attackMetaData.attackDestroyTime = value
        }

    val collidedWithWall : Boolean
        get() = this[AttackMeta].collidedWithWall

    val hasNoBlinkComp : Boolean
        get() = this.getOrNull(Blink) == null

    fun addBlinkComp(maxTime : Float,blinkRatio : Float){
        entity.configure {
            it += Blink(maxTime, blinkRatio)
        }
    }

    fun addCollectable() {

        entity.configure {
            it += EntityTag.COLLECTABLE
            it += Collectable(GameObject.SWORD)
        }
    }

    fun remove(){
        GameEventDispatcher.fireEvent(GameEvent.RemoveEntityEvent(entity))
    }

}
