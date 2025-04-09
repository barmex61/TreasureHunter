package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

data class AttackMeta(
    val isMelee: Boolean,
    val owner: Entity,
    val attackType: AttackType,
    var currentFrameIndex: Int = -1,
    var isFixtureMirrored : Boolean,
    var hasFixture : Boolean = false,
    var attackDestroyCooldown : Float = attackType.attackDestroyCooldown,
    var collidedWithWall : Boolean = false
) : Component<AttackMeta> {
    override fun type() = AttackMeta
    companion object : ComponentType<AttackMeta>()

}
