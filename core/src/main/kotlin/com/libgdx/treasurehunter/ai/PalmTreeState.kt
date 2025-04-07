package com.libgdx.treasurehunter.ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.libgdx.treasurehunter.ecs.components.AnimationType

enum class PalmTreeState : EntityState {

    IDLE {
        override fun enter(entity: AiEntity) {
            entity.animation(AnimationType.IDLE, PlayMode.LOOP)
        }
    },
}
