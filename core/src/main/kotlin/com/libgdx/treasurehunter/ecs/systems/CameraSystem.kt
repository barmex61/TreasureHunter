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
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEvent.MapChangeEvent
import com.libgdx.treasurehunter.event.GameEventListener
import ktx.tiled.height
import ktx.tiled.width

class CameraSystem(
    private val gameCamera: OrthographicCamera = World.inject()
) : IteratingSystem(family = family { all(Graphic, EntityTag.PLAYER) }), GameEventListener {

    private val mapBoundaries = Vector2(0f, 0f)
    val viewportW = gameCamera.viewportWidth * 0.5f
    val viewportH = gameCamera.viewportHeight * 0.5f
    init {
        gameCamera.apply {
            zoom = 0.65f
        }
    }

    override fun onTickEntity(entity: Entity) {
        val (sprite) = entity[Graphic]
        var camX = sprite.x + sprite.width * 0.5f
        var camY = sprite.y + sprite.height * 0.5f

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
        }
    }
}
