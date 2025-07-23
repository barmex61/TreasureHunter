package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Flash
import com.libgdx.treasurehunter.ecs.components.FlashType
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.enums.InterpolateChannels
import kotlin.math.sin


class FlashSystem : IteratingSystem(family { all(Flash, Graphic) }) {
    override fun onTickEntity(entity: Entity) {
        val flashCmp = entity[Flash]
        when(flashCmp.flashType){
            FlashType.CONTINUOUS_WAVE -> {
                handleContinuousWave( flashCmp)
            }
            FlashType.BLINK -> {
                handleBlink(entity, flashCmp)
            }
            FlashType.ONCE -> {
                handleOnce()
            }
        }
    }

    private fun handleContinuousWave(flashCmp : Flash){
        var (shaderEffect, _, _, _, _, _, _, _,shaderEffectData,timer) = flashCmp
        timer = (timer + world.deltaTime * shaderEffectData.frequency) % 360f
        val sin = sin(timer)
        val centerG = shaderEffect.shaderEffectData.greenTint
        val centerB = shaderEffect.shaderEffectData.blueTint
        val centerR = shaderEffect.shaderEffectData.redTint
        val amplitude = shaderEffectData.amplitude

        val interpolateChannels = shaderEffectData.shaderInterpolateChannels
        if (InterpolateChannels.RED in interpolateChannels) {
            shaderEffectData.redTint = centerR + (sin * amplitude)
        }
        if (InterpolateChannels.GREEN in interpolateChannels) {
            shaderEffectData.greenTint = centerG + (sin * amplitude)
        }
        if (InterpolateChannels.BLUE in interpolateChannels) {
            shaderEffectData.blueTint = centerB + (sin * amplitude)
        }
        flashCmp.shaderEffectData = shaderEffectData
        flashCmp.timer = timer
    }

    private fun handleBlink(entity: Entity,flashCmp: Flash){
        var (_, flashDuration, flashDurationTimer,flashInterval,flashIntervalTimer,flashTimer,doFlash, _, _, _) = flashCmp
        flashTimer -= deltaTime
        if (flashTimer <= 0){
            entity.configure {
                it -= Flash
            }
            flashCmp.doFlash = false
            return
        }
        if (doFlash){
            flashDurationTimer -= deltaTime
            doFlash = flashDurationTimer >= 0f
        }else{
            if (flashIntervalTimer >= 0f){
                flashIntervalTimer -= deltaTime
            }else{
                doFlash = true
                flashIntervalTimer = flashInterval
                flashDurationTimer = flashDuration
            }
        }

        flashCmp.flashIntervalTimer = flashIntervalTimer
        flashCmp.doFlash = doFlash
        flashCmp.flashTimer = flashTimer
        flashCmp.flashDurationTimer = flashDurationTimer
    }

    private fun handleOnce(){
        return
    }


}
