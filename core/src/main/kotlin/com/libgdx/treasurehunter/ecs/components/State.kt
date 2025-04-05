package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.ai.AiEntity
import com.libgdx.treasurehunter.ai.EntityState

typealias AiState = com.badlogic.gdx.ai.fsm.State<AiEntity>

data class State(
    val owner : AiEntity,
    val initialState : EntityState,
    val stateMachine : DefaultStateMachine<AiEntity,AiState> = DefaultStateMachine(owner).apply { changeState(initialState) }
) : Component <State> {

    override fun type() = State

    companion object : ComponentType<State>()

}
