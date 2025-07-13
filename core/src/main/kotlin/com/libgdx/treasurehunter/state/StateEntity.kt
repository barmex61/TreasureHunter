package com.libgdx.treasurehunter.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityUpdateContext
import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ecs.components.AiComponent
import com.libgdx.treasurehunter.ecs.components.AiState
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationData
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.AttackMeta
import com.libgdx.treasurehunter.ecs.components.Blink
import com.libgdx.treasurehunter.ecs.components.Chest
import com.libgdx.treasurehunter.ecs.components.DamageTaken
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Inventory
import com.libgdx.treasurehunter.ecs.components.Item
import com.libgdx.treasurehunter.ecs.components.Mark
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.components.Ship
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.utils.animation
import com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.MarkType
import com.libgdx.treasurehunter.enums.ParticleType
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.state.StateEntity
import com.libgdx.treasurehunter.tiled.sprite
import com.libgdx.treasurehunter.utils.GameObject
import com.libgdx.treasurehunter.utils.distance
import ktx.math.vec2
import ktx.math.minus
import kotlin.math.pow
import kotlin.math.sin
import kotlin.text.get
import com.libgdx.treasurehunter.utils.plus
import ktx.app.gdxError
import kotlin.collections.plusAssign


sealed class StateEntity(
    val entity: Entity,
    val world: World,
    physicWorld: PhysicWorld,
    val assetHelper: AssetHelper,
) {

    inline operator fun <reified T : Component<*>> Entity.get(type: ComponentType<T>): T =
        with(world) {
            return this@get[type]
        }

    inline operator fun <reified T : Component<*>> get(type: ComponentType<T>): T = with(world) {
        return entity[type]
    }

    inline fun <reified T : Component<*>> getOrNull(type: ComponentType<T>): T? = with(world) {
        return entity.getOrNull(type)
    }


    fun Entity.configure(configuration: EntityUpdateContext.(Entity) -> Unit) =
        with(world) { this@configure.configure(configuration) }

    val body: Body
        get() = this[Physic].body

    val doAttack: Boolean
        get() {
            var doAttack = this.getOrNull(Attack)?.doAttack ?: return false
            return if (doAttack) {
                this[Attack].doAttack = false
                true
            } else false
        }


    val isGetHit: Boolean
        get() = getOrNull(DamageTaken) != null

    var alpha: Float
        get() = this.getOrNull(Graphic)?.sprite?.color?.a ?: 1f
        set(value) {
            this.getOrNull(Graphic)?.sprite?.setAlpha(value)
        }

    val animKeyFrameIx: Int
        get() {
            return this.getOrNull(Animation)?.getAnimKeyFrameIx() ?: 0
        }

    val animType: AnimationType
        get() = this.getOrNull(Animation)?.animationData?.animationType ?: AnimationType.NONE

    fun animation(
        animationType: AnimationType,
        playMode: PlayMode = PlayMode.LOOP,
        frameDuration: Float? = null,
    ) = with(world) {
        animation(entity, animationType, playMode, frameDuration)
    }


    fun <T : StateEntity> state(state: EntityState<T>, toPreviousState: Boolean = false) {
        val stateComp = this[State]
        val nextState = if (toPreviousState) stateComp.stateMachine.previousState else state
        this[State].stateMachine.changeState(nextState as AiState)
    }

    fun isAnimationDone(): Boolean = with(world) {
        return@with get(Animation).isAnimationDone()
    }

    fun remove() {
        entity.configure {
            it += EntityTag.REMOVE
        }
    }

    class PlayerEntity(
        entity: Entity,
        world: World,
        physicWorld: PhysicWorld,
        assetHelper: AssetHelper,
    ) : StateEntity(entity, world, physicWorld, assetHelper) {
        var runParticleTimer: Float = 0.5f

        val state: PlayerState
            get() = this[State].stateMachine.currentState as PlayerState

        fun fireParticleEvent(particleType: ParticleType) {
            GameEventDispatcher.fireEvent(GameEvent.ParticleEvent(entity, particleType))
        }

        fun removeDamageTaken() {
            val damageTaken = getOrNull(DamageTaken) ?: return
            if (damageTaken.isContinuous) return
            entity.configure {
                it -= DamageTaken
            }
        }

        fun createMarkEntity(markType: MarkType) {
            entity.configure { aiEntity ->
                if (entity hasNo EntityTag.HAS_MARK) {
                    aiEntity += EntityTag.HAS_MARK
                    val markPosition = entity[Graphic].center
                    val gameObjectFromType = markType.toGameObject()
                    world.entity { markEntity ->
                        markEntity += Graphic(
                            sprite(
                                gameObjectFromType.atlasKey,
                                AnimationType.IN,
                                markPosition,
                                assetHelper,
                                0f
                            )
                        )
                        markEntity += Animation(
                            gameObjectFromType.atlasKey, AnimationData(
                                animationType = AnimationType.IN,
                                playMode = PlayMode.NORMAL,
                                frameDuration = 0.2f
                            )
                        )
                        markEntity += Mark(1f, markType.offset, aiEntity)
                    }
                }
            }
        }

    }

    class SwordEntity(
        entity: Entity,
        world: World,
        physicWorld: PhysicWorld,
        assetHelper: AssetHelper,
    ) : StateEntity(entity, world, physicWorld, assetHelper) {
        val isCollected: Boolean
            get() {
                with(world) {
                    return entity has EntityTag.COLLECTED
                }
            }

        var attackDestroyTimer: Float
            get() = this[AttackMeta].attackMetaData.attackDestroyTime
            set(value) {
                this[AttackMeta].attackMetaData.attackDestroyTime = value
            }

        val collidedWithWall: Boolean
            get() = this[AttackMeta].collidedWithWall

        val hasNoBlinkComp: Boolean
            get() = this.getOrNull(Blink) == null

        fun addBlinkComp(maxTime: Float, blinkRatio: Float) {
            entity.configure {
                it += Blink(maxTime, blinkRatio)
            }
        }

        fun setSpriteOffset() {
            val graphic = get(Graphic)
            val xOffset = if (graphic.sprite.isFlipX) -0.18f else 0.18f
            graphic.offset += vec2(xOffset, 0.07f)
        }

    }

    class ShipEntity(
        entity: Entity,
        world: World,
        physicWorld: PhysicWorld,
        assetHelper: AssetHelper,
    ) : StateEntity(entity, world, physicWorld, assetHelper) {
        private val ship: Ship
            get() = this[Ship]

        private val attachedEntities
            get() = with(world) {
                family { all(EntityTag.ATTACH_TO_SHIP) }
            }.map()

        private fun Family.map(): MutableMap<Entity, Vector2> {
            val attachedEntityMap = mutableMapOf<Entity, Vector2>()
            forEach { entity ->
                attachedEntityMap[entity] =
                    entity[Physic].body.position - this@ShipEntity.entity[Physic].body.position
            }
            return attachedEntityMap
        }

        private var waveTime: Float = 0f
        private val waveAmplitude: Float = 0.1f
        private val waveFrequency: Float = 2.5f
        private val originalY: Float = body.position.y

        fun waveEffect(deltaTime: Float) {
            if (ship.isWaveEffectEnabled) {
                waveTime += deltaTime
                val waveOffset = sin(waveTime * waveFrequency) * waveAmplitude
                val newY = originalY + waveOffset

                body.setTransform(body.position.x, newY, body.angle)
                updateAttachedEntities(Gdx.graphics.deltaTime)
            }
        }

        fun updateAttachedEntities(deltaTime: Float) {

            val shipTransform = body.position
            if (ship.attachedEntities.isEmpty()) {
                ship.attachedEntities = attachedEntities
            }
            ship.attachedEntities.forEach { (entity, offset) ->
                val attachedBody = entity[Physic].body

                attachedBody.setTransform(
                    shipTransform.x + offset.x,
                    shipTransform.y + offset.y,
                    attachedBody.angle
                )
            }
        }
    }

    class ChestEntity(
        entity: Entity,
        world: World,
        physicWorld: PhysicWorld,
        assetHelper: AssetHelper,
    ): StateEntity(entity, world, physicWorld, assetHelper){

        var isOpened: Boolean
            get() = this[Chest].isOpened
            set(value) {
                this[Chest].isOpened = value
            }

        val openAnimType : AnimationType
            get() {
                val chest = this[Chest]
                return when(chest.gameObject){
                    GameObject.CHEST -> AnimationType.OPEN
                    GameObject.CHEST_LOCKED -> if (!chest.isUnlockAnimPlayed) {
                        chest.isUnlockAnimPlayed = true
                        AnimationType.UNLOCK
                    } else AnimationType.REUNLOCK
                    else -> gdxError("Unknown chest type: ${chest.gameObject}")
                }
            }

        val closeAnimType : AnimationType
            get() {
                val chest = this[Chest]
                return when(chest.gameObject){
                    GameObject.CHEST -> AnimationType.CLOSE
                    GameObject.CHEST_LOCKED -> AnimationType.LOCK
                    else -> gdxError("Unknown chest type: ${chest.gameObject}")
                }
            }

    }

}
