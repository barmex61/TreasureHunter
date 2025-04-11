package com.libgdx.treasurehunter.ai

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.EntityTag


enum class SwordState : EntityState{

    IDLE{
      override fun enter(entity: AiEntity) {
          entity.animation(AnimationType.IDLE)
          entity.addCollectable()
        }
        override fun update(entity: AiEntity) {
            if (entity.isCollected) {
                entity.alpha = 0f
                entity.state(RESPAWN)
            }
        }
    },

    RESPAWN{
        var respawnTimer = 2f
        override fun enter(entity: AiEntity) {
            respawnTimer = 2f
            with(entity.world){
                entity.entity.configure {
                    it -= EntityTag.COLLECTABLE
                }
            }
        }
        override fun update(entity: AiEntity) {
            respawnTimer -= Gdx.graphics.deltaTime
            if (respawnTimer <= 0f){
                entity.state(IDLE)
                entity.alpha = 1f
            }
        }
    },

    SPINNING{
        var spinningDuration = 3f
        override fun enter(entity: AiEntity) {
            spinningDuration = 3f
            entity.animation(AnimationType.SPINNING)
        }

        override fun update(entity: AiEntity) {
            spinningDuration -= Gdx.graphics.deltaTime
            when{
                entity.collidedWithWall -> entity.state(EMBEDDED)
                spinningDuration <= 0f -> {
                    entity.attackDestroyCooldown = 0f
                    entity.remove()
                }
            }
        }
    },
    EMBEDDED{
        override fun enter(entity: AiEntity) {
            entity.addCollectable()
            entity.animation(AnimationType.EMBEDDED, playMode = Animation.PlayMode.NORMAL)
        }
        override fun update(entity: AiEntity) {
            entity.attackDestroyCooldown -= entity.world.deltaTime
            when{
                entity.attackDestroyCooldown <= 1f && entity.hasNoBlinkComp -> entity.addBlinkComp(1f,0.075f)
                entity.attackDestroyCooldown <= 0f || entity.isCollected -> entity.remove()
            }
        }
    }
}
