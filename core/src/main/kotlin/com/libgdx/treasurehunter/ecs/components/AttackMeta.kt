package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ecs.systems.AttackHandler
import com.libgdx.treasurehunter.ecs.systems.MeleeAttackHandler
import com.libgdx.treasurehunter.ecs.systems.RangeAttackHandler

data class AttackMeta(
    val owner: Entity,
    var currentFrameIndex: Int = -1,
    var isFixtureMirrored : Boolean,
    var collidedWithWall : Boolean = false,
    var attackMetaData : AttackMetaData,
    var attackHandler : AttackHandler? = null

) : Component<AttackMeta> {


    fun setAttackHandler(textureAtlas: TextureAtlas,world: World) {
        if (attackHandler != null) return
        attackHandler = when (attackMetaData.attackType.isMelee) {
            true -> MeleeAttackHandler(textureAtlas,world)
            false -> RangeAttackHandler(world)
        }
    }

    override fun type() = AttackMeta
    companion object : ComponentType<AttackMeta>()

}
