package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.ecs.systems.AttackHandler
import com.libgdx.treasurehunter.ecs.systems.MeleeAttackHandler
import com.libgdx.treasurehunter.ecs.systems.RangeAttackHandler

data class AttackMeta(
    val owner: Entity,
    var currentFrameIndex: Int = 0,
    var isFixtureMirrored : Boolean,
    var hasFixture : Boolean = false,
    var collidedWithWall : Boolean = false,
    var attackMetaData : AttackMetaData,

) : Component<AttackMeta> {
    var attackHandler : AttackHandler = if (attackMetaData.isMelee) MeleeAttackHandler() else RangeAttackHandler()
        get() {
            if ((attackMetaData.isMelee && field !is MeleeAttackHandler) || (!attackMetaData.isMelee && field !is RangeAttackHandler)) {
                field = if (attackMetaData.isMelee) MeleeAttackHandler() else RangeAttackHandler()
            }
            return field
        }


    override fun type() = AttackMeta
    companion object : ComponentType<AttackMeta>()

}
