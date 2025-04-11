package com.libgdx.treasurehunter.ai

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.MathUtils
import com.libgdx.treasurehunter.ai.EntityState.Companion.TOLERANCE_X
import com.libgdx.treasurehunter.ai.EntityState.Companion.TOLERANCE_Y
import com.libgdx.treasurehunter.ai.EntityState.Companion.ZERO
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.AttackItem
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.enums.MarkType
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.utils.GameObject
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
            entity.fireParticleEvent()
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
            entity.createMarkEntity(MarkType.EXCLAMATION_MARK)
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
    SWORD_COLLECTED{
        override fun enter(aiEntity: AiEntity) {
            with(aiEntity.world){
                if (aiEntity.entity hasNo Attack){
                    aiEntity.entity.configure {
                        it += Attack(attackItem = AttackItem.SWORD)
                        it[Graphic].gameObject = GameObject.CAPTAIN_CLOWN_SWORD
                        it[com.libgdx.treasurehunter.ecs.components.Animation].setNewGameObject(GameObject.CAPTAIN_CLOWN_SWORD)
                    }
                }
            }
        }

        override fun update(aiEntity: AiEntity) {
            with(aiEntity.world) {
                aiEntity.entity[State].stateMachine.changeState(IDLE)
            }
        }
    },
    SWORD_THROWED{
        override fun enter(aiEntity: AiEntity) {
            with(aiEntity.world){
                if (aiEntity.entity has Attack){
                    aiEntity.entity.configure {
                        it -= Attack
                        it[Graphic].gameObject = GameObject.CAPTAIN_CLOWN
                        it[com.libgdx.treasurehunter.ecs.components.Animation].setNewGameObject(GameObject.CAPTAIN_CLOWN)
                    }
                }
            }
        }
        override fun update(aiEntity: AiEntity) {
            with(aiEntity.world) {
                aiEntity.entity[State].stateMachine.changeState(IDLE)
            }
        }
    }

}
