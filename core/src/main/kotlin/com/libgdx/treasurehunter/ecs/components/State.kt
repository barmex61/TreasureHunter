package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.state.StateEntity
import com.libgdx.treasurehunter.state.EntityState

typealias AiState = com.badlogic.gdx.ai.fsm.State<StateEntity>

data class State(
    val owner : StateEntity,
    val initialState : EntityState,
    val stateMachine : DefaultStateMachine<StateEntity,AiState> = DefaultStateMachine(owner).apply { changeState(initialState) }
) : Component <State> {

    override fun type() = State

    companion object : ComponentType<State>()

}
