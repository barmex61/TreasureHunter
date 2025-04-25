package com.libgdx.treasurehunter.state

import com.badlogic.gdx.Gdx
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.GdxAnimation
import com.libgdx.treasurehunter.state.StateEntity.*

enum class ShipState : EntityState<ShipEntity> {
    IDLE{
        override fun enter(entity: ShipEntity) {
            entity.animation(AnimationType.IDLE)
        }

        override fun update(entity: ShipEntity) {
            entity.waveEffect(Gdx.graphics.deltaTime)
        }
    }
}
