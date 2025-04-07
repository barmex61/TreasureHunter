package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntityBy
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.systems.CameraSystem.Companion.cameraPos
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventListener
import com.libgdx.treasurehunter.utils.Constants
import ktx.app.gdxError
import ktx.graphics.use

class RenderSystem (
    private val spriteBatch: SpriteBatch = inject(),
    private val gameViewPort : StretchViewport = inject(),
    private val gameCamera : OrthographicCamera = inject()
): IntervalSystem(enabled = true), GameEventListener {

    private val orthogonalTiledMapRenderer by lazy {
        OrthogonalTiledMapRenderer(null, Constants.UNIT_SCALE, spriteBatch)
    }
    private val entityComparator = compareEntityBy(Graphic)
    private val entities = family{all(Graphic).none(EntityTag.BACKGROUND,EntityTag.FOREGROUND)}
    private val backgroundEntities = family { all(Graphic,EntityTag.BACKGROUND) }
    private val foregroundEntities = family { all(Graphic,EntityTag.FOREGROUND) }
    private var groundLayer : TiledMapTileLayer? = null
    private var backgroundClose : TiledMapTileLayer? = null
    private var backgroundFar : TiledMapTileLayer? = null
    private var backgroundMid : TiledMapTileLayer? = null

    override fun onTick() {
        gameViewPort.apply()
        orthogonalTiledMapRenderer.setView(gameCamera)
        spriteBatch.use {

            orthogonalTiledMapRenderer.renderTileLayer(backgroundFar)
            orthogonalTiledMapRenderer.renderTileLayer(backgroundMid)
            orthogonalTiledMapRenderer.renderTileLayer(backgroundClose)
            backgroundEntities.renderEntities()
            orthogonalTiledMapRenderer.renderTileLayer(groundLayer)
            entities.renderEntities()
            foregroundEntities.renderEntities()
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
                try {
                    event.tiledMap.layers.forEach {
                        println(it.name)
                    }
                    groundLayer = event.tiledMap.layers.get("ground") as TiledMapTileLayer
                    backgroundClose = event.tiledMap.layers.get("background_close") as TiledMapTileLayer
                    backgroundFar = event.tiledMap.layers.get("background_far") as TiledMapTileLayer
                    backgroundMid = event.tiledMap.layers.get("background_mid") as TiledMapTileLayer

                }catch (e: Exception){
                    gdxError("There is no layer name registerede for $e in tiledMap ${event.tiledMap}")
                }

            }
        }
    }

}
