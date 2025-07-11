package com.libgdx.treasurehunter.state

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Item
import com.libgdx.treasurehunter.ecs.components.ItemData
import com.libgdx.treasurehunter.ecs.components.Sword
import ktx.math.vec2


enum class SwordState : EntityState<StateEntity.SwordEntity> {

    IDLE {
        override fun enter(entity: StateEntity.SwordEntity) {
            entity.animation(AnimationType.IDLE)
        }

        override fun update(entity: StateEntity.SwordEntity) {
            if (entity.isCollected){
                entity.alpha = 0f
                entity.state(RESPAWN)
            }
        }
    },

    RESPAWN {
        var respawnTimer = 2f
        override fun enter(entity: StateEntity.SwordEntity) {
            respawnTimer = 2f

        }

        override fun update(entity: StateEntity.SwordEntity) {
            respawnTimer -= Gdx.graphics.deltaTime
            if (respawnTimer <= 0f) {

                entity.state(IDLE)
                entity.alpha = 1f
                with(entity.world) {
                    entity.entity.configure {
                        it -= EntityTag.COLLECTED
                    }
                }
            }
        }
    },

    SPINNING {
        var spinningDuration = 3f
        override fun enter(entity: StateEntity.SwordEntity) {
            spinningDuration = 3f
            entity.animation(AnimationType.SPINNING)
        }

        override fun update(entity: StateEntity.SwordEntity) {
            spinningDuration -= Gdx.graphics.deltaTime
            when {
                entity.collidedWithWall -> entity.state(EMBEDDED)
                spinningDuration <= 0f -> {
                    entity.attackDestroyTimer = 0f
                    entity.remove()
                }
            }
        }
    },
    EMBEDDED {

        override fun enter(entity: StateEntity.SwordEntity) {
            with(entity.world){
                entity.entity.configure {
                    it += Item(ItemData(Sword()))
                }
            }
            entity.animation(AnimationType.EMBEDDED, playMode = Animation.PlayMode.NORMAL)
            entity.setSpriteOffset()
        }

        override fun update(entity: StateEntity.SwordEntity) {
            entity.attackDestroyTimer -= entity.world.deltaTime
            when {
                entity.attackDestroyTimer <= 1f && entity.hasNoBlinkComp -> entity.addBlinkComp(
                    1f,
                    0.075f
                )
                entity.attackDestroyTimer <= 0f || entity.isCollected -> entity.remove()
            }
        }

        override fun exit(entity: StateEntity.SwordEntity) {
            with(entity.world){
                entity.entity.configure {
                    it -= Item
                }
            }
        }
    }
}
