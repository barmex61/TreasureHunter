package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Flash
import com.libgdx.treasurehunter.ecs.components.Graphic


class FlashSystem : IteratingSystem(family { all(Flash, Graphic) }) {

    override fun onTickEntity(entity: Entity) {
        val flashCmp = entity[Flash]
        var (color, flashAmount, flashDuration, flashDurationTimer,flashInterval,flashIntervalTimer,doFlash) = flashCmp
        if (flashAmount <= 0){
            entity.configure {
                it -= Flash
            }
        }
        if (doFlash){
            flashDurationTimer -= deltaTime
            doFlash = flashDurationTimer >= 0f
        }else{
            if (flashIntervalTimer >= 0f){
                flashIntervalTimer -= deltaTime
            }else{
                doFlash = true
                flashAmount -= 1
                flashIntervalTimer = flashInterval
                flashDurationTimer = flashDuration
            }
        }

        flashCmp.flashIntervalTimer = flashIntervalTimer
        flashCmp.doFlash = doFlash
        flashCmp.flashAmount = flashAmount
        flashCmp.flashDurationTimer = flashDurationTimer
    }

}
