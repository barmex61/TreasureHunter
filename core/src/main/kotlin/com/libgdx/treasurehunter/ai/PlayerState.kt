package com.libgdx.treasurehunter.ai

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.MathUtils
import com.libgdx.treasurehunter.ai.EntityState.Companion.TOLERANCE_X
import com.libgdx.treasurehunter.ai.EntityState.Companion.TOLERANCE_Y
import com.libgdx.treasurehunter.ai.EntityState.Companion.ZERO
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Physic
import ktx.math.component1
import ktx.math.component2

enum class PlayerState : EntityState {
    APPEARING{
        override fun enter(entity: AiEntity) {
            entity[Move].stop = true
        }
        override fun update(entity: AiEntity) {
            if (entity.isAnimationDone()) {
                entity[Move].stop = false
                entity.state(IDLE)
            }

        }
    },
    IDLE{
        override fun enter(entity: AiEntity) {
            val (linX,linY) = entity.body.linearVelocity
            val TOLERANCE_X = 0.0f
            when{
                linY > TOLERANCE_Y -> entity.state(JUMP)
                linY < -TOLERANCE_Y -> entity.state(FALL)
                !MathUtils.isEqual(linX, ZERO, TOLERANCE_X) -> entity.state(RUN)
                else ->  entity.animation(AnimationType.IDLE)
            }
        }

        override fun update(entity: AiEntity) {
            val (linX,linY) = entity.body.linearVelocity
            when{
                entity.wantsToAttack -> entity.state(ATTACK)
                linY > TOLERANCE_Y -> entity.state(JUMP)
                linY < -TOLERANCE_Y -> entity.state(FALL)
                !MathUtils.isEqual(linX, ZERO, TOLERANCE_X) -> entity.state(RUN)
            }
        }
    },

    RUN {
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.RUN)
        }

        override fun update(entity: AiEntity) {
            val (linX,linY) = entity.body.linearVelocity
            when{
                entity.wantsToAttack -> entity.state(ATTACK)
                linY > TOLERANCE_Y -> entity.state(JUMP)
                linY < -TOLERANCE_Y -> entity.state(FALL)
                MathUtils.isEqual(linX, ZERO, TOLERANCE_X) -> entity.state(IDLE)
            }
        }
    },

    JUMP {
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.JUMP, Animation.PlayMode.NORMAL)
        }

        override fun update(entity: AiEntity) {
            val (linX,linY) = entity.body.linearVelocity
            when{
                entity.wantsToAttack -> entity.state(ATTACK)
                linY < -TOLERANCE_Y -> entity.state(FALL)
                MathUtils.isEqual(linY, ZERO, TOLERANCE_Y) ->{
                    if (MathUtils.isEqual(linX, ZERO, TOLERANCE_X)){
                        entity.state(IDLE)
                    }else{
                        entity.state(RUN)
                    }
                }
            }
        }
    },

    FALL {
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.FALL)
        }

        override fun update(entity: AiEntity) {
            val (linX,linY) = entity.body.linearVelocity
            when{
                entity.wantsToAttack -> entity.state(ATTACK)
                linY > TOLERANCE_Y -> entity.state(JUMP)
                MathUtils.isEqual(linY, ZERO, TOLERANCE_Y) ->{
                    if (MathUtils.isEqual(linX, ZERO, TOLERANCE_X)){
                        entity.state(IDLE)
                    }else{
                        entity.state(RUN)
                    }
                }
            }
        }
    },

    HIT {
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.HIT, Animation.PlayMode.NORMAL)
        }

        override fun update(entity: AiEntity) {
            when{
                entity.wantsToAttack -> entity.state(ATTACK)
                entity.isAnimationDone() -> entity.state(IDLE)
            }
        }
    },

    ATTACK{
        override fun enter(entity: AiEntity) {
            val animType = entity[Attack].attackType.animType
            entity.animation(animType, Animation.PlayMode.NORMAL,0.16f)
        }

        override fun update(entity: AiEntity) {
            if (entity.isAnimationDone()) entity.state(IDLE)
        }
    },


}
