package com.libgdx.treasurehunter.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.MathUtils
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.Item
import com.libgdx.treasurehunter.ecs.components.ItemType
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.ecs.components.Sword
import com.libgdx.treasurehunter.enums.MarkType
import com.libgdx.treasurehunter.enums.ParticleType
import com.libgdx.treasurehunter.utils.GameObject
import ktx.math.component1
import ktx.math.component2

enum class PlayerState : EntityState {
    APPEARING{
        override fun enter(entity: StateEntity) {
            entity[Move].stop = true
        }
        override fun update(entity: StateEntity) {
            if (entity.isAnimationDone()) {
                entity[Move].stop = false
                entity.state(IDLE)
            }

        }
    },
    IDLE{
        override fun enter(entity: StateEntity) {
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

        override fun update(entity: StateEntity) {
            val (linX,linY) = entity.body.linearVelocity
            when{
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
        override fun enter(entity: StateEntity) {
            entity.animation(AnimationType.RUN)
            entity.fireParticleEvent(ParticleType.RUN)
            entity.runParticleTimer = 0.5f
        }

        override fun update(entity: StateEntity) {
            entity.runParticleTimer -= Gdx.graphics.deltaTime
            if (entity.runParticleTimer <= 0f){
                entity.runParticleTimer = 0.5f
                entity.fireParticleEvent(ParticleType.RUN)
            }
            val (linX,linY) = entity.body.linearVelocity
            when{
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
        override fun enter(entity: StateEntity) {
            entity.animation(AnimationType.JUMP, Animation.PlayMode.NORMAL)
        }

        override fun update(entity: StateEntity) {
            val (linX,linY) = entity.body.linearVelocity
            when{
                entity.doAttack -> entity.state(ATTACK)
                linY < -EntityState.Companion.TOLERANCE_Y -> entity.state(FALL)
                MathUtils.isEqual(linY,
                    EntityState.Companion.ZERO,
                    EntityState.Companion.TOLERANCE_Y
                ) ->{
                    if (MathUtils.isEqual(linX,
                            EntityState.Companion.ZERO,
                            EntityState.Companion.TOLERANCE_X
                        )){
                        entity.state(IDLE)
                    }
                }
            }
        }
    },

    FALL {
        override fun enter(entity: StateEntity) {
            entity.animation(AnimationType.FALL)
        }

        override fun update(entity: StateEntity) {
            val (linX,linY) = entity.body.linearVelocity
            when{
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
        override fun enter(entity: StateEntity) {
            entity.animation(AnimationType.GROUND, playMode = Animation.PlayMode.NORMAL)
            entity.fireParticleEvent(ParticleType.FALL)
        }

        override fun update(entity: StateEntity) {
            if (entity.isAnimationDone()){
                val (linX,linY) = entity.body.linearVelocity
                when{
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
        override fun enter(entity: StateEntity) {
            entity.animation(AnimationType.HIT, Animation.PlayMode.NORMAL)
            entity.createMarkEntity(MarkType.EXCLAMATION_MARK)
        }

        override fun update(entity: StateEntity) {
            when{
                entity.doAttack -> entity.state(ATTACK)
                entity.isAnimationDone() -> entity.state(IDLE)
            }
        }
    },

    ATTACK{
        override fun enter(entity: StateEntity) {
            val animType = entity[Attack].attackMetaData.attackType.attackAnimType
            entity.animation(animType, Animation.PlayMode.NORMAL,0.16f)
        }

        override fun update(entity: StateEntity) {
            if (entity.isAnimationDone()) entity.state(IDLE)
        }
    },

    SWORD_COLLECTED{
        override fun enter(stateEntity: StateEntity) {
            with(stateEntity.world){
                if (stateEntity.entity hasNo Attack){
                    stateEntity.entity.configure {
                        it += Item(
                            itemType = Sword(),
                            owner = stateEntity.entity
                        )
                        it[com.libgdx.treasurehunter.ecs.components.Animation].setNewGameObject(GameObject.CAPTAIN_CLOWN_SWORD)
                    }
                }
            }
        }

        override fun update(stateEntity: StateEntity) {
            with(stateEntity.world) {
                stateEntity.entity[State].stateMachine.changeState(IDLE)
            }
        }
    },
    SWORD_THROWED{
        override fun enter(stateEntity: StateEntity) {
            with(stateEntity.world){
                if (stateEntity.entity has Attack){
                    stateEntity.entity.configure {
                        it -= Attack
                        it[com.libgdx.treasurehunter.ecs.components.Animation].setNewGameObject(GameObject.CAPTAIN_CLOWN)
                    }
                }
            }
        }
        override fun update(stateEntity: StateEntity) {
            with(stateEntity.world) {
                stateEntity.entity[State].stateMachine.changeState(IDLE)
            }
        }
    }

}
