package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.physics.box2d.Body
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World

enum class AttackState {
    READY,
    ATTACKING,
    DONE,
}

data class Attack(
    val attackDamage: Float = 0f,
    var wantsToAttack : Boolean = false,
    var attackState : AttackState = AttackState.READY,
    var attackBody : Body? = null,
    var attackDuration : Float = 1f
) : Component <Attack> {


    override fun World.onRemove(entity: Entity) {
        attackBody?.world?.destroyBody(attackBody)
    }
    override fun type(): ComponentType<Attack> = Attack

    companion object : ComponentType<Attack>()

}
