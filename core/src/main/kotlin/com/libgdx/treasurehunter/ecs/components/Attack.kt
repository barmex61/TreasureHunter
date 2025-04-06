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

//NEED TO IMPROVE ATTACK NAMES I DIDNT KNOW WHAT TO PUT.NEED TO CHECK WHAT KIND OF ANIMATIONS THEY ARE
enum class AttackType(val animType : AnimationType) {
    FIRST_ATTACK(AnimationType.ATTACK_1),
    SECONDARY_ATTACK(AnimationType.ATTACK_2),
    THIRD_ATTACK(AnimationType.ATTACK_3)
}

data class Attack(
    val attackDamage: Float = 0f,
    var wantsToAttack : Boolean = false,
    var attackState : AttackState = AttackState.READY,
    var attackBody : Body? = null,
    var attackDuration : Float = 1f,
    var attackType : AttackType = AttackType.FIRST_ATTACK

) : Component <Attack> {


    override fun World.onRemove(entity: Entity) {
        attackBody?.world?.destroyBody(attackBody)
    }
    override fun type(): ComponentType<Attack> = Attack

    companion object : ComponentType<Attack>()

}
