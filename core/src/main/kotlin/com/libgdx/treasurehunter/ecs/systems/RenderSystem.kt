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
import ktx.graphics.use
import ktx.math.random
import ktx.tiled.height
import ktx.tiled.layer
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


    override fun onTick() {
        gameViewPort.apply()

        orthogonalTiledMapRenderer.setView(gameCamera)
        orthogonalTiledMapRenderer.render()
        spriteBatch.use {
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
                try {
                    orthogonalTiledMapRenderer.map = event.tiledMap
                }catch (e: Exception){
                    gdxError("There is no layer name registerede for $e in tiledMap ${event.tiledMap}")
                }

            }
        }
    }

}
