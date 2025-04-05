package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEvent.MapChangeEvent
import com.libgdx.treasurehunter.event.GameEventListener
import com.libgdx.treasurehunter.utils.Constants
import ktx.tiled.height
import ktx.tiled.width
import kotlin.math.max

class CameraSystem(
    private val gameCamera: OrthographicCamera = World.inject()
) : IteratingSystem(family = family { all(Graphic, EntityTag.PLAYER) }), GameEventListener {

    private val mapBoundaries = Vector2(0f, 0f)
    val viewportW = gameCamera.viewportWidth * 0.5f
    val viewportH = gameCamera.viewportHeight * 0.5f

    override fun onTickEntity(entity: Entity) {
        val (sprite) = entity[Graphic]
        var camX = sprite.x + sprite.width * 0.5f
        var camY = sprite.y + sprite.height * 0.5f

        if (!mapBoundaries.isZero) {


            camX = camX.coerceIn(viewportW, maxOf(mapBoundaries.x - viewportW, viewportW))
            camY = camY.coerceIn(viewportH, maxOf(mapBoundaries.y - viewportH, viewportH))
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
        }
    }
}
