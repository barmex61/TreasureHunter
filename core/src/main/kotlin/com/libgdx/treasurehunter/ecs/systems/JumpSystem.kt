package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.Body
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Jump
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.utils.Constants
import ktx.math.component1
import ktx.math.component2

class JumpSystem(
    private val physicWorld: PhysicWorld = World.inject(),
): IteratingSystem(family = family { all(Jump) }) {


    override fun onTickEntity(entity: Entity) {
        val jumpComp = entity[Jump]
        val (body,_) = entity[Physic]
        var (maxHeight,wantsJump,canJump,doubleJump,yImpulse) = entity[Jump]

        if (!wantsJump || !canJump) return
        body.setLinearVelocity(body.linearVelocity.x,yImpulse)
        jumpComp.wantsJump = false
        jumpComp.canJump = false

    }


}
