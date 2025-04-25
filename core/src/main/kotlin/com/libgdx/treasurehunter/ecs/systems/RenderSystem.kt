package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapLayer
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntityBy
import com.libgdx.treasurehunter.ecs.components.AttackMeta
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Flash
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventListener
import com.libgdx.treasurehunter.utils.Constants
import ktx.app.gdxError
import ktx.graphics.use
import com.libgdx.treasurehunter.utils.ColorSettings
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.ShaderAsset
import com.libgdx.treasurehunter.enums.ShaderEffect
import com.libgdx.treasurehunter.shaders.ShaderManager
import ktx.graphics.copy

class RenderSystem (
    private val spriteBatch: SpriteBatch = inject(),
    private val gameViewPort : StretchViewport = inject(),
    private val gameCamera : OrthographicCamera = inject(),
    assetHelper: AssetHelper = inject()
): IntervalSystem(enabled = true), GameEventListener {

    private val entityComparator = compareEntityBy(Graphic)
    private val entities = family{all(Graphic).none(EntityTag.BACKGROUND,EntityTag.FOREGROUND)}
    private val backgroundEntities = family { all(Graphic,EntityTag.BACKGROUND) }
    private val foregroundEntities = family { all(Graphic,EntityTag.FOREGROUND) }

    // Static background layers
    private var groundLayer : TiledMapTileLayer? = null
    private var backgroundClose : TiledMapTileLayer? = null
    private var backgroundFar : TiledMapTileLayer? = null
    private var backgroundMid : TiledMapTileLayer? = null

    private var currentColorSettings: ColorSettings = ColorSettings.DAY
    private val shaderManager: ShaderManager = ShaderManager(assetHelper[ShaderAsset.FLASH], onShaderEffectChange = { shaderProgram ->
        spriteBatch.shader = shaderProgram
    })
    private val mapRenderer: OrthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(null, Constants.UNIT_SCALE, spriteBatch).apply {
        setView(gameCamera)
    }
    private var mapInitialized : Boolean = false

    override fun onTick() {
        AnimatedTiledMapTile.updateAnimationBaseTime()
        if (!mapInitialized) return
        // Update parallax offsets
        backgroundClose?.offsetX -= deltaTime * 10f

        gameViewPort.apply()
        mapRenderer.setView(gameCamera)
        spriteBatch.use {
            // Render background layers in order
            mapRenderer.renderTileLayer(backgroundFar)
            mapRenderer.renderTileLayer(backgroundMid)
            mapRenderer.renderTileLayer(backgroundClose)


            // Render background entities
            backgroundEntities.renderEntities()

            // Render ground and main entities
            mapRenderer.renderTileLayer(groundLayer)
            entities.renderEntities()

            // Render foreground elements

            foregroundEntities.renderEntities()
        }
    }

    private fun Family.renderEntities() {
        sort(entityComparator)
        forEach { entity ->
            val sprite = entity[Graphic].sprite
            sprite.color = currentColorSettings.entityColor.copy(alpha = sprite.color.a)
            val flashCmp = entity.getOrNull(Flash)
            if (flashCmp != null && flashCmp.doFlash) {
                sprite.color = flashCmp.color
            }
            sprite.draw(spriteBatch)
        }
    }

    private fun applyColorSettings(settings: ColorSettings) {
        backgroundFar?.let {
            it.opacity = settings.backgroundFarColor.a
            it.tintColor = settings.backgroundFarColor
        }

        backgroundMid?.let {
            it.opacity = settings.backgroundMidColor.a
            it.tintColor = settings.backgroundMidColor
        }

        backgroundClose?.let {
            it.opacity = settings.backgroundCloseColor.a
            it.tintColor = settings.backgroundCloseColor
        }

        groundLayer?.let {
            it.opacity = settings.groundColor.a
            it.tintColor = settings.groundColor
        }
    }

    override fun onEvent(event: GameEvent) {
        when(event) {
            is GameEvent.MapChangeEvent -> {
                mapInitialized = true
                groundLayer = event.tiledMap.layers.get("ground") as TiledMapTileLayer
                backgroundClose = (event.tiledMap.layers.get("background_close") as TiledMapTileLayer).apply {
                    parallaxX = parallaxMap["background_close"]!!.first
                    parallaxY = parallaxMap["background_close"]!!.second
                }
                backgroundFar = (event.tiledMap.layers.get("background_far") as TiledMapTileLayer).apply {
                    parallaxX = parallaxMap["background_far"]!!.first
                    parallaxY = parallaxMap["background_far"]!!.second
                }
                backgroundMid = (event.tiledMap.layers.get("background_mid") as TiledMapTileLayer).apply {
                    parallaxX = parallaxMap["background_mid"]!!.first
                    parallaxY = parallaxMap["background_mid"]!!.second
                }
            }
            else -> Unit
        }
    }

    private val parallaxMap = mapOf<String, Pair<Float, Float>>(
        "background_close" to Pair(0.5f, 0.5f),
        "background_mid" to Pair(0.3f, 0.3f),
        "background_far" to Pair(0f, 0f)
    )

    private fun setCurrentSettings(displayMode : String){
        currentColorSettings = ColorSettings.valueOf(displayMode)
        setShaderEffectFromColorSettings()
        applyColorSettings(currentColorSettings)
    }

    fun setShaderEffect(shaderEffect: ShaderEffect){
        shaderManager.applyShaderEffect(shaderEffect)
    }

    fun setShaderEffectFromColorSettings(){
        shaderManager.applyShaderEffect(currentColorSettings.shaderEffect)
    }
}
