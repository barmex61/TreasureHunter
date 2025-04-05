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
import ktx.app.gdxError
import ktx.tiled.height
import ktx.tiled.use
import ktx.tiled.width

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

    private var bgCloseLayer : TiledMapTileLayer? = null
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
            renderParallaxLayer(bgFarLayer, originalX, originalY)
            renderParallaxLayer(bgMidLayer, originalX, originalY)
            renderParallaxLayer(bgCloseLayer, originalX, originalY)
            renderParallaxLayer(ground, originalX, originalY)
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

    private fun renderParallaxLayer(layer: TiledMapTileLayer?, originalX: Float, originalY: Float) {
        layer?:return
        val parallaxX = layer.parallaxX
        val parallaxY = layer.parallaxY

        val newX = originalX * parallaxX
        val newY = originalY * parallaxY

        gameCamera.position.set(newX, newY, 0f)
        gameCamera.update()
        orthogonalTiledMapRenderer.setView(gameCamera)
        orthogonalTiledMapRenderer.renderTileLayer(layer)
    }

    override fun onEvent(event: GameEvent) {
        when(event) {
            is GameEvent.MapChangeEvent -> {
                try {
                    bgFarLayer = event.tiledMap.layers.get(LayerNames.BACKGROUND_FAR.toString()) as? TiledMapTileLayer
                    bgMidLayer = event.tiledMap.layers.get(LayerNames.BACKGROUND_MID.toString()) as? TiledMapTileLayer
                    bgCloseLayer = event.tiledMap.layers.get(LayerNames.BACKGROUND_CLOSE.toString()) as? TiledMapTileLayer
                    ground = event.tiledMap.layers.get(LayerNames.GROUND.toString()) as? TiledMapTileLayer
                }catch (e: Exception){
                    gdxError("There is no layer name registerede for $e in tiledMap ${event.tiledMap}")
                }

                mapBoundaries.set(
                    event.tiledMap.width.toFloat(), event.tiledMap.height.toFloat()
                )
            }
        }
    }

    enum class LayerNames{
        BACKGROUND_FAR(),
        BACKGROUND_MID(),
        BACKGROUND_CLOSE(),
        GROUND();

        override fun toString() = this.name.lowercase()
    }
}
