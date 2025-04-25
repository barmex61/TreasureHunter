package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.MoveDirection
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEvent.MapChangeEvent
import com.libgdx.treasurehunter.event.GameEventListener
import ktx.tiled.height
import ktx.tiled.width

class CameraSystem(
    private val gameCamera: OrthographicCamera = World.inject()
) : IteratingSystem(family = family { all(Graphic, EntityTag.PLAYER) }), GameEventListener {

    private val mapBoundaries = Vector2(0f, 0f)
    var viewportW = gameCamera.viewportWidth * 0.5f
    var viewportH = gameCamera.viewportHeight * 0.5f
    var cameraMovement : CameraMovement = CameraMovement(0,0)
    var zoom : Float = 0f

    private fun initViewPort(){
        viewportW = gameCamera.viewportWidth * 0.5f
        viewportH = gameCamera.viewportHeight * 0.5f
    }

    override fun onTickEntity(entity: Entity) {
        val (sprite) = entity[Graphic]
        if (viewportW == 0f || viewportH == 0f) {
            initViewPort()
            return
        }
        var camX = sprite.x + sprite.width * 0.5f
        var camY = sprite.y + sprite.height * 0.5f
        if (zoom != 0f){
            gameCamera.zoom += zoom * 0.01f
        }
        if (cameraMovement.valueX != 0 || cameraMovement.valueY != 0) {
            val currentPosition = gameCamera.position
            currentPosition.x += cameraMovement.valueX * 0.01f
            currentPosition.y += cameraMovement.valueY * 0.01f
            gameCamera.update()
            return
        }
        if (!mapBoundaries.isZero) {
            camX = MathUtils.lerp(gameCamera.position.x,camX.coerceIn(viewportW, maxOf(mapBoundaries.x - viewportW, viewportW)),deltaTime * 4)
            camY = MathUtils.lerp(gameCamera.position.y,camY.coerceIn(viewportH, maxOf(mapBoundaries.y - viewportH, viewportH)),deltaTime * 4)
        }
        gameCamera.position.set(camX, camY, 0f)
        gameCamera.update()

    }

    override fun onEvent(event: GameEvent) {
        when (event) {
            is MapChangeEvent -> {
                mapBoundaries.set(
                    event.tiledMap.width.toFloat() , event.tiledMap.height.toFloat()
                )
            }
            else -> Unit
        }
    }
}

data class CameraMovement(
    var valueX : Int,
    var valueY : Int
){
    operator fun plusAssign(other: MoveDirection){
        valueX += other.valueX
        valueY += other.valueY
    }
    operator fun minusAssign(other: MoveDirection){
        valueX -= other.valueX
        valueY -= other.valueY
    }
}

