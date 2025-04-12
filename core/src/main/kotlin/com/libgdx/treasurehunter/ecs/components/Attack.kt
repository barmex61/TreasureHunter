package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

enum class AttackState {
    READY,
    ATTACKING,
    DONE,
}


data class Attack(
    var attackItem : ItemType.Damageable,
    var wantsToAttack : Boolean = false,
    var attackState : AttackState = AttackState.READY,
    var doAttack : Boolean = false
    ) : Component <Attack> {

    override fun type(): ComponentType<Attack> = Attack

    companion object : ComponentType<Attack>()

}
