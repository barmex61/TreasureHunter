package com.libgdx.treasurehunter.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.MathUtils
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.SlotName
import com.libgdx.treasurehunter.enums.EffectType
import com.libgdx.treasurehunter.enums.ParticleType
import com.libgdx.treasurehunter.enums.SoundAsset
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.state.StateEntity.*
import ktx.math.component1
import ktx.math.component2

enum class PlayerState : EntityState<PlayerEntity> {
    APPEARING{
        override fun enter(entity: PlayerEntity) {
            entity[Move].stop = true
        }
        override fun update(entity: PlayerEntity) {
            if (entity.isAnimationDone()) {
                entity[Move].stop = false
                entity.state(IDLE)
            }

        }
    },
    IDLE{
        override fun enter(entity: PlayerEntity) {
            val (linX,linY) = entity.body.linearVelocity
            when{
                entity.doAttack -> entity.state(ATTACK)
                linY > EntityState.Companion.TOLERANCE_Y -> entity.state(JUMP)
                linY < -EntityState.Companion.TOLERANCE_Y -> entity.state(FALL)
                !MathUtils.isEqual(linX,
                    EntityState.Companion.ZERO,
                    EntityState.Companion.TOLERANCE_X
                ) -> entity.state(RUN)
                else ->  entity.animation(AnimationType.IDLE)
            }
        }

        override fun update(entity: PlayerEntity) {
            val (linX,linY) = entity.body.linearVelocity
            when{
                entity.isDeadHit -> entity.state(DEAD_HIT)
                entity.isGetHit -> entity.state(HIT)
                entity.doAttack -> entity.state(ATTACK)
                linY > EntityState.Companion.TOLERANCE_Y -> entity.state(JUMP)
                linY < -EntityState.Companion.TOLERANCE_Y -> entity.state(FALL)
                !MathUtils.isEqual(linX,
                    EntityState.Companion.ZERO,
                    EntityState.Companion.TOLERANCE_X
                ) -> entity.state(RUN)
            }
        }
    },

    RUN {
        override fun enter(entity: PlayerEntity) {
            entity.animation(AnimationType.RUN)
            entity.fireParticleEvent(ParticleType.RUN)
            entity.runParticleTimer = 0.5f
        }

        override fun update(entity: PlayerEntity) {
            entity.runParticleTimer -= Gdx.graphics.deltaTime
            if (entity.runParticleTimer <= 0f){
                entity.runParticleTimer = 0.5f
                entity.fireParticleEvent(ParticleType.RUN)
            }
            if (entity.animKeyFrameIx == 3 && entity.animType == AnimationType.RUN){
                GameEventDispatcher.fireEvent(GameEvent.PlaySoundEvent(SoundAsset.PLAYER_FOOTSTEP))
            }
            val (linX,linY) = entity.body.linearVelocity
            when{
                entity.isDeadHit -> entity.state(DEAD_HIT)
                entity.isGetHit -> entity.state(HIT)
                entity.doAttack -> entity.state(ATTACK)
                linY > EntityState.Companion.TOLERANCE_Y -> entity.state(JUMP)
                linY < -EntityState.Companion.TOLERANCE_Y -> entity.state(FALL)
                MathUtils.isEqual(linX,
                    EntityState.Companion.ZERO,
                    EntityState.Companion.TOLERANCE_X
                ) -> entity.state(IDLE)
            }
        }
    },

    JUMP {
        override fun enter(entity: PlayerEntity) {
            entity.animation(AnimationType.JUMP, Animation.PlayMode.NORMAL)
        }

        override fun update(entity: PlayerEntity) {
            val (linX,linY) = entity.body.linearVelocity
            when{
                entity.isDeadHit -> entity.state(DEAD_HIT)
                entity.isGetHit -> entity.state(HIT)
                entity.doAttack -> entity.state(ATTACK)
                linY < -EntityState.Companion.TOLERANCE_Y -> entity.state(FALL)
                MathUtils.isEqual(linY,
                    EntityState.Companion.ZERO,
                    EntityState.Companion.TOLERANCE_Y
                ) ->{

                    entity.state(IDLE)
                }
            }
        }
    },

    FALL {
        override fun enter(entity: PlayerEntity) {
            entity.animation(AnimationType.FALL)
        }

        override fun update(entity: PlayerEntity) {
            val (linX,linY) = entity.body.linearVelocity
            when{
                entity.isDeadHit -> entity.state(DEAD_HIT)
                entity.isGetHit -> entity.state(HIT)
                entity.doAttack -> entity.state(ATTACK)
                linY > EntityState.Companion.TOLERANCE_Y -> entity.state(JUMP)
                MathUtils.isEqual(linY,
                    EntityState.Companion.ZERO,
                    EntityState.Companion.TOLERANCE_Y
                ) ->{
                    entity.state(GROUND)
                }
            }
        }
    },

    GROUND{
        override fun enter(entity: PlayerEntity) {
            entity.animation(AnimationType.GROUND, playMode = Animation.PlayMode.NORMAL)
            entity.fireParticleEvent(ParticleType.FALL)
        }

        override fun update(entity: PlayerEntity) {
            if (entity.isAnimationDone()){
                val (linX,linY) = entity.body.linearVelocity
                when{
                    entity.isDeadHit -> entity.state(DEAD_HIT)
                    entity.isGetHit -> entity.state(HIT)
                    entity.doAttack -> entity.state(ATTACK)
                    linY > EntityState.Companion.TOLERANCE_Y -> entity.state(JUMP)
                    linY < -EntityState.Companion.TOLERANCE_Y -> entity.state(FALL)
                    !MathUtils.isEqual(linX,
                        EntityState.Companion.ZERO,
                        EntityState.Companion.TOLERANCE_X
                    ) -> entity.state(RUN)
                    else -> entity.state(IDLE)
                }
            }
        }
    },

    HIT {
        override fun enter(entity: PlayerEntity) {
            entity.animation(AnimationType.HIT, Animation.PlayMode.NORMAL)
            entity.createMarkEntity(EffectType.EXCLAMATION_MARK)
        }

        override fun update(entity: PlayerEntity) {
            when{
                entity.doAttack -> entity.state(ATTACK)
                entity.isAnimationDone() -> entity.state(IDLE)
            }
        }
    },

    DEAD_HIT {
        override fun enter(entity: PlayerEntity) {
            GameEventDispatcher.fireEvent(GameEvent.EquippedItemRemoved(SlotName.SWORD.toString() ,entity.entity))
            GameEventDispatcher.fireEvent(GameEvent.PlayerDeadEvent(entity.entity))
            entity.animation(AnimationType.DEAD_HIT, Animation.PlayMode.NORMAL,0.16f)
        }

        override fun update(entity: PlayerEntity) {
            when{
                entity.isAnimationDone() -> entity.state(DEAD_GROUND)
            }
        }
    },

    DEAD_GROUND {
        override fun enter(entity: PlayerEntity) {
            entity.animation(AnimationType.DEAD_GROUND, Animation.PlayMode.NORMAL,0.16f)
        }

        override fun update(entity: PlayerEntity) {
            when{
                entity.isAnimationDone() -> {
                    entity.remove()
                }
            }
        }
    },

    ATTACK{
        override fun enter(entity: PlayerEntity) {
            val attackType = entity[Attack].attackMetaData.attackType
            val animType = attackType.attackAnimType
            entity.animation(animType, Animation.PlayMode.NORMAL,0.16f)
            if (attackType.isMelee){
                GameEventDispatcher.fireEvent(GameEvent.PlaySoundEvent(SoundAsset.SWORD_SWING))
            }
        }

        override fun update(entity: PlayerEntity) {
            when{

                entity.isAnimationDone() -> {
                    val animType = entity[Attack].attackMetaData.attackType.attackAnimType
                    when(animType){
                        AnimationType.THROW->{
                            GameEventDispatcher.fireEvent(GameEvent.EquippedItemRemoved(SlotName.SWORD.toString() ,entity.entity))
                        }
                        else -> Unit
                    }
                    entity.state(IDLE)
                }
            }
        }
    },

}
