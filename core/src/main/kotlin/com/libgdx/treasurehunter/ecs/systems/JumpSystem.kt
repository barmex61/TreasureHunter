package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Jump
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.systems.DebugSystem.Companion.JUMP_DEBUG_RECT
import com.libgdx.treasurehunter.ecs.systems.PhysicSystem.Companion.isChest
import com.libgdx.treasurehunter.ecs.systems.PhysicSystem.Companion.isFlag
import com.libgdx.treasurehunter.ecs.systems.PhysicSystem.Companion.isGround
import com.libgdx.treasurehunter.ecs.systems.PhysicSystem.Companion.isPlatform
import com.libgdx.treasurehunter.ecs.systems.PhysicSystem.Companion.isShipHelm
import com.libgdx.treasurehunter.enums.ParticleType
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.game.PhysicWorld
import ktx.box2d.query
import ktx.math.plus
import ktx.math.minus
import ktx.math.vec2

class JumpSystem(
    private val physicWorld: PhysicWorld = World.inject(),
): IteratingSystem(family = family { all(Jump) }) {


    override fun onTickEntity(entity: Entity) {
        val jumpComp = entity[Jump]
        val (body,_) = entity[Physic]
        var (maxHeight,lowerXY, upperXY,wantsJump,doubleJump,yImpulse) = entity[Jump]
        if (!wantsJump ) return
        val bodyPosition = body.position
        val jumpRectLowerXY = bodyPosition + lowerXY
        val jumpRectUpperXY = bodyPosition + upperXY
        val jumpRectWidth = jumpRectUpperXY.x - jumpRectLowerXY.x
        val jumpRectHeight = jumpRectUpperXY.y - jumpRectUpperXY.y
        JUMP_DEBUG_RECT.set(jumpRectLowerXY.x,jumpRectLowerXY.y,jumpRectWidth,jumpRectHeight)
        physicWorld.query(jumpRectLowerXY.x,jumpRectLowerXY.y + 0.05f,jumpRectUpperXY.x,jumpRectUpperXY.y) { collisionFixture ->
            if (collisionFixture.isJumpable()){
                jump(entity,jumpComp,body,yImpulse)
                return@query false
            }
            return@query true
        }
    }

    private fun Fixture.isJumpable() = this.isGround || this.isFlag || this.isPlatform || this.isShipHelm || this.isChest

    private fun jump(entity: Entity,jumpComp: Jump,jumpBody : Body,yImpulse : Float) {
        jumpBody.setLinearVelocity(jumpBody.linearVelocity.x,yImpulse)
        jumpComp.wantsJump = false
        GameEventDispatcher.fireEvent(GameEvent.ParticleEvent(entity, ParticleType.JUMP))
    }
}
