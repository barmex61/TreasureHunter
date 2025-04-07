package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.github.quillraven.fleks.Family
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntityBy
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

class RenderSystem (
    private val spriteBatch: SpriteBatch = inject(),
    private val gameViewPort : StretchViewport = inject(),
    private val gameCamera : OrthographicCamera = inject(),
    private val assetHelper: AssetHelper = inject()
): IntervalSystem(enabled = true), GameEventListener {

    private val entityComparator = compareEntityBy(Graphic)
    private val entities = family{all(Graphic).none(EntityTag.BACKGROUND,EntityTag.FOREGROUND)}
    private val backgroundEntities = family { all(Graphic,EntityTag.BACKGROUND) }
    private val foregroundEntities = family { all(Graphic,EntityTag.FOREGROUND) }
    private var groundLayer : TiledMapTileLayer? = null
    private var backgroundClose : TiledMapTileLayer? = null
    private var backgroundFar : TiledMapTileLayer? = null
    private var backgroundMid : TiledMapTileLayer? = null
    private var currentColorSettings: ColorSettings = ColorSettings.DAY
    private val shaderManager: ShaderManager = ShaderManager(assetHelper[ShaderAsset.FLASH])
    private val mapRenderer: OrthogonalTiledMapRenderer = OrthogonalTiledMapRenderer(null, Constants.UNIT_SCALE, spriteBatch).apply {
        setView(gameCamera)
    }

    override fun onTick() {
        gameViewPort.apply()
        mapRenderer.setView(gameCamera)

        spriteBatch.shader = shaderManager.shader
        spriteBatch.use {
            mapRenderer.renderTileLayer(backgroundFar)
            mapRenderer.renderTileLayer(backgroundMid)
            mapRenderer.renderTileLayer(backgroundClose)
            backgroundEntities.renderEntities()
            mapRenderer.renderTileLayer(groundLayer)
            entities.renderEntities()
            foregroundEntities.renderEntities()
        }
        spriteBatch.shader = null
    }

    private fun Family.renderEntities() {
        sort(entityComparator)

        val previousShaderEffect = shaderManager.currentShaderEffect

        forEach { entity ->
            val sprite = entity[Graphic].sprite
            sprite.color = currentColorSettings.entityColor

            val flashCmp = entity.getOrNull(Flash)
            if (flashCmp != null && flashCmp.doFlash) {
                println("APPPLY")
                sprite.color = flashCmp.color
                //shaderManager.applyShaderEffect(ShaderEffect.BURN_EFFECT)
            } else {
                previousShaderEffect?.let {
                    //shaderManager.applyShaderEffect(it)
                }
            }
            spriteBatch.shader = shaderManager.shader
            entity[Graphic].sprite.draw(spriteBatch)
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
                try {
                    groundLayer = event.tiledMap.layers.get("ground") as TiledMapTileLayer
                    backgroundClose = event.tiledMap.layers.get("background_close") as TiledMapTileLayer
                    backgroundFar = event.tiledMap.layers.get("background_far") as TiledMapTileLayer
                    backgroundMid = event.tiledMap.layers.get("background_mid") as TiledMapTileLayer
                    val displayMode = event.tiledMap.properties.get("mapDisplayMode", ColorSettings.DAY.name, String::class.java)
                    setCurrentSettings(displayMode)
                } catch (e: Exception) {
                    gdxError("There is no layer name registerede for $e in tiledMap ${event.tiledMap}")
                }
            }
            else -> Unit
        }
    }

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
