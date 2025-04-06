package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Jump
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.systems.DebugSystem.Companion.JUMP_DEBUG_RECT
import com.libgdx.treasurehunter.ecs.systems.DebugSystem.Companion.JUMP_FOOT_DEBUG_VECTOR
import com.libgdx.treasurehunter.ecs.systems.PhysicSystem.Companion.isGround
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.utils.plus
import com.libgdx.treasurehunter.utils.minus
import ktx.box2d.query
import ktx.math.vec2
import kotlin.Float.Companion.MAX_VALUE
import kotlin.Float.Companion.MIN_VALUE
import kotlin.math.abs

class JumpSystem(
    private val physicWorld: PhysicWorld = World.inject(),
): IteratingSystem(family = family { all(Jump) }) {


    override fun onTickEntity(entity: Entity) {
        val jumpComp = entity[Jump]
        val (body,_) = entity[Physic]
        var (maxHeight,lowerXY, upperXY,wantsJump,doubleJump,yImpulse) = entity[Jump]

        if (!wantsJump ) return
        val bodyPosition = body.position
        val footRectLowerXy = (bodyPosition + lowerXY)
        val footRectUpperXy = (bodyPosition + upperXY)
        val footRectWidth = footRectUpperXy.x - footRectLowerXy.x
        val footRectHeight = footRectUpperXy.y - footRectLowerXy.y
        //FOR DEBUGGING
        JUMP_DEBUG_RECT.set(footRectLowerXy.x,footRectLowerXy.y,footRectWidth,footRectHeight)

        physicWorld.query(footRectLowerXy.x,footRectLowerXy.y,footRectUpperXy.x,footRectUpperXy.y){ collisionFixture ->


            if (collisionFixture.isGround) {
                // ------ NORM VECTOR BECAUSE WE DONT WANT SIDE GROUND JUMP AND ABS TO MAKE IT POSITIVE------
                var groundBodyPosition = collisionFixture.body.position
                val groundFixtureShape = collisionFixture.body.fixtureList.first().shape as PolygonShape
                val boxWidth = groundFixtureShape.radius
                val maxVertex = vec2()
                (0..groundFixtureShape.vertexCount - 1).forEach{
                    val vertex = vec2()
                    groundFixtureShape.getVertex(it,vertex)
                    if (vertex.x >= maxVertex.x && vertex.y >= maxVertex.y){
                        maxVertex.set(vertex)
                    }
                }
                groundBodyPosition += maxVertex
                println("groundbodyposition: $groundBodyPosition")
                val directionVectorNormalized = Vector2(
                    (footRectLowerXy.x + footRectWidth/2f) -  groundBodyPosition.x ,
                    (footRectLowerXy.y + footRectHeight/2f) - groundBodyPosition.y
                ).nor()
                JUMP_FOOT_DEBUG_VECTOR = floatArrayOf(
                    groundBodyPosition.x,
                    groundBodyPosition.y,
                    (footRectLowerXy.x + footRectWidth/2f),
                    (footRectLowerXy.y + footRectHeight/2f)
                )
                println(directionVectorNormalized.y)
                if (directionVectorNormalized.y  > 0f ){
                    jump(jumpComp,body,yImpulse)
                    return@query false
                }
            }
            return@query true
        }
    }

    private fun jump(jumpComp: Jump,jumpBody : Body,yImpulse : Float) {
        jumpBody.setLinearVelocity(jumpBody.linearVelocity.x,yImpulse)
        jumpComp.wantsJump = false
    }
}
