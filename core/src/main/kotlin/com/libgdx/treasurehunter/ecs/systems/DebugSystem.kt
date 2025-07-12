package com.libgdx.treasurehunter.ecs.systems
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Polyline
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.utils.GameProperties
import ktx.graphics.use

class DebugSystem(
    private val physicWorld : World = inject(),
    private val gameCamera : OrthographicCamera = inject(),
    private val gameProperties: GameProperties = inject()
) : IntervalSystem() {

    private val box2DDebugRenderer = Box2DDebugRenderer().apply {
        SHAPE_STATIC.set(Color.BLUE)
    }

    private val shapeRenderer = ShapeRenderer()

    override fun onTick() {
        if (gameProperties.debugPhysic) box2DDebugRenderer.render(physicWorld, gameCamera.combined)
        shapeRenderer.use(ShapeRenderer.ShapeType.Line,gameCamera.combined){
            it.color = Color.RED
            it.rect(JUMP_DEBUG_RECT.x, JUMP_DEBUG_RECT.y, JUMP_DEBUG_RECT.width, JUMP_DEBUG_RECT.height)
            it.rect(BODY_POSITION_DEBUG_RECT.x, BODY_POSITION_DEBUG_RECT.y, BODY_POSITION_DEBUG_RECT.width, BODY_POSITION_DEBUG_RECT.height)
            if (RAY_CAST_POLYLINE.vertices.isNotEmpty()) it.polyline(RAY_CAST_POLYLINE.vertices)
            if (JUMP_DEBUG_VECTOR_NORM.isNotEmpty()) it.polyline(JUMP_DEBUG_VECTOR_NORM)
        }
    }

    override fun onDispose() {
        box2DDebugRenderer.dispose()
    }

    companion object{
        val BODY_POSITION_DEBUG_RECT = Rectangle()
        val RAY_CAST_POLYLINE = Polyline()
        val JUMP_DEBUG_RECT = Rectangle()
        var JUMP_DEBUG_VECTOR_NORM = floatArrayOf()

    }
}

