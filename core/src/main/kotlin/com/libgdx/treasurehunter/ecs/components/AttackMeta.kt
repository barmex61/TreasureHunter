package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity

data class AttackMeta(
    val owner: Entity,
    var currentFrameIndex: Int = -1,
    var isFixtureMirrored : Boolean,
    var hasFixture : Boolean = false,
    var collidedWithWall : Boolean = false,
    var attackItem : ItemType.Damageable

) : Component<AttackMeta> {
    override fun type() = AttackMeta
    companion object : ComponentType<AttackMeta>()

}
