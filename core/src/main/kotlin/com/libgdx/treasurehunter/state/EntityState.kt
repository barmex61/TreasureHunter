package com.libgdx.treasurehunter.state

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram

interface EntityState : State<StateEntity>{
    companion object{
        const val TOLERANCE_X = 0.15f
        const val TOLERANCE_Y = 0.8f
        const val ZERO = 0f
    }
    override fun enter(entity: StateEntity) = Unit
    override fun update(entity: StateEntity) = Unit
    override fun exit(entity: StateEntity) = Unit
    override fun onMessage(entity: StateEntity?, telegram: Telegram?) = false
}
