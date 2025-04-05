package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.ecs.systems.AttackSystem.Companion.DEBUG_RECT
import ktx.graphics.use

class DebugSystem(
    private val physicWorld : World = inject(),
    private val gameCamera : OrthographicCamera = inject()
) : IntervalSystem() {

    private val box2DDebugRenderer = Box2DDebugRenderer().apply {
        SHAPE_STATIC.set(Color.RED)
    }

    private val shapeRenderer = ShapeRenderer()

    override fun onTick() {
        box2DDebugRenderer.render(physicWorld, gameCamera.combined)
        shapeRenderer.use(ShapeRenderer.ShapeType.Line,gameCamera.combined){
            it.color = Color.RED
            it.rect(DEBUG_RECT.x, DEBUG_RECT.y, DEBUG_RECT.width, DEBUG_RECT.height)
        }
    }

    override fun onDispose() {
        box2DDebugRenderer.dispose()
    }


}

