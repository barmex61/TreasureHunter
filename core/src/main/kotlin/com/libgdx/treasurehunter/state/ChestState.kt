package com.libgdx.treasurehunter.state

import com.badlogic.gdx.graphics.g2d.Animation

enum class ChestState : EntityState<StateEntity.ChestEntity> {
    IDLE{

        override fun update(entity: StateEntity.ChestEntity) {
            if (entity.isOpened){
                entity.state(OPEN)
            }
        }

    },

    OPEN{
        override fun enter(entity: StateEntity.ChestEntity) {
            val openAnimType = entity.openAnimType
            println(openAnimType)
            entity.animation(openAnimType,Animation.PlayMode.NORMAL)
        }
        override fun update(entity: StateEntity.ChestEntity) {
            if (!entity.isOpened){
                entity.state(CLOSE)
            }
        }
    } ,
    CLOSE{
        override fun enter(entity: StateEntity.ChestEntity) {
            val closeAnimType = entity.closeAnimType
            println(closeAnimType)
            entity.animation(closeAnimType, Animation.PlayMode.NORMAL)
        }
        override fun update(entity: StateEntity.ChestEntity) {
            if (entity.isOpened){
                entity.state(OPEN)
            }
        }
    }
}
