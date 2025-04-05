package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.github.quillraven.fleks.EachFrame
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntityBy
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventListener
import com.libgdx.treasurehunter.utils.Constants
import ktx.graphics.use
import ktx.tiled.height
import ktx.tiled.use
import ktx.tiled.width
import kotlin.comparisons.maxOf
import kotlin.ranges.coerceIn

class RenderSystem (
    private val spriteBatch: SpriteBatch = inject(),
    private val gameViewPort : StretchViewport = inject(),
    private val gameCamera : OrthographicCamera = inject()
): IntervalSystem(enabled = true), GameEventListener {

    private val orthogonalTiledMapRenderer by lazy {
        OrthogonalTiledMapRenderer(null, Constants.UNIT_SCALE, spriteBatch)
    }
    private val entityComparator = compareEntityBy(Graphic)
    private val entities = family{all(Graphic)}

    // Parallax katmanları için referanslar
    private var bgFarLayer: TiledMapTileLayer? = null
    private var bgMidLayer: TiledMapTileLayer? = null
    private var ground: TiledMapTileLayer? = null

    private val mapBoundaries = Vector2(0f, 0f)
    private var viewportW = gameCamera.viewportWidth * 0.5f
    private var viewportH = gameCamera.viewportHeight * 0.5f

    override fun onTick() {
        gameViewPort.apply()

        val originalX = gameCamera.position.x
        val originalY = gameCamera.position.y
        viewportW = gameCamera.viewportWidth * 0.5f
        viewportH = gameCamera.viewportHeight * 0.5f

        orthogonalTiledMapRenderer.setView(gameCamera)
        orthogonalTiledMapRenderer.use {
            orthogonalTiledMapRenderer.map?.layers?.forEach { layer ->
                if (layer is TiledMapTileLayer) {
                    val parallaxX = layer.properties.get("parallaxX", Float::class.java) ?: 1f
                    val parallaxY = layer.properties.get("parallaxY", Float::class.java) ?: 1f

                    val newX = originalX * parallaxX
                    val newY = originalY * parallaxY

                    gameCamera.position.set(newX, newY, 0f)
                    gameCamera.update()
                    orthogonalTiledMapRenderer.setView(gameCamera)
                    orthogonalTiledMapRenderer.renderTileLayer(layer)
                }
            }

            gameCamera.position.set(originalX, originalY, 0f)
            gameCamera.update()
            orthogonalTiledMapRenderer.setView(gameCamera)

            entities.renderEntities()
        }
    }

    private fun Family.renderEntities() {
        sort(entityComparator)
        forEach { entity ->
            entity[Graphic].sprite.draw(spriteBatch)
        }
    }

    override fun onEvent(event: GameEvent) {
        when(event) {
            is GameEvent.MapChangeEvent -> {
                orthogonalTiledMapRenderer.map = event.tiledMap
                bgFarLayer = event.tiledMap.layers.get("background_far") as? TiledMapTileLayer
                bgMidLayer = event.tiledMap.layers.get("background_mid") as? TiledMapTileLayer
                ground = event.tiledMap.layers.get("ground") as? TiledMapTileLayer
                mapBoundaries.set(
                    event.tiledMap.width.toFloat(), event.tiledMap.height.toFloat()
                )
            }
        }
    }
}
