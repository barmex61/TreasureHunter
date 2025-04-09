package com.libgdx.treasurehunter.ai

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram

interface EntityState : State<AiEntity>{
    companion object{
        const val TOLERANCE_X = 0.15f
        const val TOLERANCE_Y = 1f
        const val ZERO = 0f
    }
    override fun enter(entity: AiEntity) = Unit
    override fun update(entity: AiEntity) = Unit
    override fun exit(entity: AiEntity) = Unit
    override fun onMessage(entity: AiEntity?, telegram: Telegram?) = false
}
