package com.libgdx.treasurehunter.state



interface EntityState<T : StateEntity> : com.badlogic.gdx.ai.fsm.State<T> {
    companion object {
        const val TOLERANCE_X = 0.15f
        const val TOLERANCE_Y = 0.8f
        const val ZERO = 0f
    }

    override fun enter(entity: T) = Unit
    override fun update(entity: T) = Unit
    override fun exit(entity: T) = Unit
    override fun onMessage(entity: T?, telegram: com.badlogic.gdx.ai.msg.Telegram?) = false
}
