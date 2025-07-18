package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Flash
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.enums.InterpolateChannels
import kotlin.math.sin


class FlashSystem : IteratingSystem(family { all(Flash, Graphic) }) {
    override fun onTickEntity(entity: Entity) {
        val flashCmp = entity[Flash]
        var (shaderEffect, flashAmount, flashDuration, flashDurationTimer,flashInterval,flashIntervalTimer,doFlash,isContinuous,shaderEffectData,timer) = flashCmp
        if (isContinuous && shaderEffectData != null){
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
            return
        }
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
