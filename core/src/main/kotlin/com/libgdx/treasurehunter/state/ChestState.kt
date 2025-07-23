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
            entity.animation(openAnimType,Animation.PlayMode.NORMAL)
        }
        override fun update(entity: StateEntity.ChestEntity) {
            when{

                entity.itemAppearInterval > 0f  && !entity.isItemsSpawned -> {
                    entity.itemAppearInterval = (entity.itemAppearInterval - entity.world.deltaTime).coerceAtLeast(0f)
                }
                entity.itemAppearInterval <= 0f && !entity.isItemsSpawned-> {
                    entity.spawnItems()
                }
                !entity.isOpened -> entity.state(CLOSE)
            }
        }
    } ,
    CLOSE{
        override fun enter(entity: StateEntity.ChestEntity) {
            val closeAnimType = entity.closeAnimType
            entity.animation(closeAnimType, Animation.PlayMode.NORMAL)
        }
        override fun update(entity: StateEntity.ChestEntity) {
            when{
                entity.itemAppearInterval > 0f && !entity.isItemsSpawned -> {
                    entity.itemAppearInterval = (entity.itemAppearInterval - entity.world.deltaTime).coerceAtLeast(0f)
                }
                entity.itemAppearInterval <= 0f && !entity.isItemsSpawned -> {
                    entity.spawnItems()
                }
                entity.isOpened -> entity.state(OPEN)
            }
        }
    }
}
