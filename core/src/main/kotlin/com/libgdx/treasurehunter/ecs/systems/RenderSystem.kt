package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
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
import ktx.graphics.use
import com.libgdx.treasurehunter.utils.ColorSettings
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.ShaderAsset
import com.libgdx.treasurehunter.enums.ShaderEffect
import com.libgdx.treasurehunter.shaders.ShaderManager
import com.libgdx.treasurehunter.utils.Constants.UNIT_SCALE
import ktx.graphics.copy
import com.libgdx.treasurehunter.ecs.systems.RenderSystem.LayerType.*
import ktx.app.gdxError
import kotlin.to

class RenderSystem (
    private val spriteBatch: SpriteBatch = inject(),
    private val gameViewPort : StretchViewport = inject("gameViewPort"),
    private val uiViewport: StretchViewport = inject("uiViewPort"),
    private val stage : Stage = inject(),
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
    private var lastCellPositionBgClose = Int.MIN_VALUE
    private var lastCellPositionBgMid = Int.MIN_VALUE

    override fun onTick() {
        backgroundClose?.offsetX -= deltaTime * 10f
        resetLayerOffset(backgroundClose?.offsetX, BACKGROUND_CLOSE)
        backgroundMid?.offsetX -= deltaTime * 5f
        resetLayerOffset(backgroundMid?.offsetX, BACKGROUND_MID)
        gameViewPort.apply()
        uiViewport.apply()
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
        stage.act(deltaTime)
        stage.draw()
    }

    private fun Family.renderEntities() {
        sort(entityComparator)
        forEach { entity ->
            val sprite = entity[Graphic].sprite
            sprite.color = currentColorSettings.entityColor.copy(
                green = sprite.color.g * currentColorSettings.entityColor.g,
                red = sprite.color.r * currentColorSettings.entityColor.r,
                blue = sprite.color.b * currentColorSettings.entityColor.b,
                alpha = sprite.color.a)
            val flashCmp = entity.getOrNull(Flash)
            if (flashCmp != null && flashCmp.doFlash) {
                spriteBatch.end()
                val effectData = flashCmp.shaderEffectData ?: flashCmp.shaderEffect.shaderEffectData
                shaderManager.applyShaderEffect(effectData)
                spriteBatch.begin()
                sprite.draw(spriteBatch)
                spriteBatch.end()
                shaderManager.applyShaderEffect(currentColorSettings.shaderEffect.shaderEffectData)
                spriteBatch.begin()
            } else {
                sprite.draw(spriteBatch)
            }
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

    private fun resetLayerOffset(layerOffsetX: Float?, layerType: LayerType) {
        val (getLayer,setLayerOffset) = when (layerType) {
            BACKGROUND_CLOSE -> {
                {backgroundClose} to { offset : Float -> backgroundClose?.offsetX = offset }
            }
            BACKGROUND_MID -> {
                {backgroundMid} to { offset : Float -> backgroundMid?.offsetX = offset }
            }
            else -> gdxError("Unsupported layer type for last cell position: $layerType")
        }
        if (layerOffsetX == null || getLayer() == null) return
        val offsetScaled = layerOffsetX * UNIT_SCALE
        val lastCellPosition = if (layerType == BACKGROUND_CLOSE) lastCellPositionBgClose else lastCellPositionBgMid
        if (offsetScaled < -lastCellPosition) {
            setLayerOffset(getLayer()!!.width.toFloat() / UNIT_SCALE )
        }
    }
    override fun onEvent(event: GameEvent) {
        when(event) {
            is GameEvent.MapChangeEvent -> {
                groundLayer = event.tiledMap.layers.get(GROUND.layerName) as TiledMapTileLayer
                backgroundClose = (event.tiledMap.layers.get(BACKGROUND_CLOSE.layerName) as TiledMapTileLayer).apply {
                    parallaxX = parallaxMap[BACKGROUND_CLOSE.layerName]!!.first
                    parallaxY = parallaxMap[BACKGROUND_CLOSE.layerName]!!.second
                    setLastCellPosition(BACKGROUND_CLOSE)
                }
                backgroundMid = (event.tiledMap.layers.get(BACKGROUND_MID.layerName) as TiledMapTileLayer).apply {
                    parallaxX = parallaxMap[BACKGROUND_MID.layerName]!!.first
                    parallaxY = parallaxMap[BACKGROUND_MID.layerName]!!.second
                    setLastCellPosition(BACKGROUND_MID)
                }
                backgroundFar = (event.tiledMap.layers.get(BACKGROUND_FAR.layerName) as TiledMapTileLayer).apply {
                    parallaxX = parallaxMap[BACKGROUND_FAR.layerName]!!.first
                    parallaxY = parallaxMap[BACKGROUND_FAR.layerName]!!.second
                }
                val mapDisplayMode = event.tiledMap.properties.get("mapDisplayMode", String::class.java) ?: ColorSettings.DAY.name
                setCurrentSettings(mapDisplayMode)
            }
            else -> Unit
        }
    }

    private fun TiledMapTileLayer.setLastCellPosition(layerType: LayerType) {
        val (getCellPosition,setCellPosition) = when (layerType) {
            BACKGROUND_CLOSE -> {
                {lastCellPositionBgClose} to { cell : Int-> lastCellPositionBgClose = cell }
            }
            BACKGROUND_MID -> {
                {lastCellPositionBgMid} to { cell : Int-> lastCellPositionBgMid = cell }
            }
            else -> gdxError("Unsupported layer type for last cell position: $layerType")
        }
        for (x in 0 .. this.width){
            for (y in 0 .. this.height){
                val cell = this.getCell(x,y)
                if (cell != null){
                    if (x > getCellPosition()){
                        setCellPosition(x)
                    }
                }
            }
        }
    }

    private val parallaxMap = mapOf(
        BACKGROUND_CLOSE.layerName to Pair(0.5f, 0.5f),
        BACKGROUND_MID.layerName to Pair(0.3f, 0.3f),
        BACKGROUND_FAR.layerName to Pair(0f, 0f)
    )

    private fun setCurrentSettings(displayMode : String){
        currentColorSettings = ColorSettings.valueOf(displayMode)
        setShaderEffectFromColorSettings()
        applyColorSettings(currentColorSettings)
    }

    fun setShaderEffectFromColorSettings(){
        shaderManager.applyShaderEffect(currentColorSettings.shaderEffect.shaderEffectData)
    }


    enum class LayerType{
        GROUND,
        BACKGROUND_CLOSE,
        BACKGROUND_MID,
        BACKGROUND_FAR;
        val layerName = this.name.lowercase()
    }
}
