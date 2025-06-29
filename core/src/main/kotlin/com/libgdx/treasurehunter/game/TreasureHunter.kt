package com.libgdx.treasurehunter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.PropertiesUtils
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.libgdx.treasurehunter.audio.AudioManager
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.game.screens.LoadingScreen
import com.libgdx.treasurehunter.utils.Constants
import com.libgdx.treasurehunter.utils.GamePreferences
import com.libgdx.treasurehunter.utils.GameProperties
import com.libgdx.treasurehunter.utils.toGameProperties
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld

val inputMultiplexer : InputMultiplexer
    get() = Gdx.input.inputProcessor as InputMultiplexer

typealias PhysicWorld = World

class TreasureHunter : KtxGame<KtxScreen>() {

    private val physicWorld = createWorld(gravity = Constants.GRAVITY, allowSleep = true).apply {
        autoClearForces = true
    }

    private val gameProperties : GameProperties by lazy {
        val propertiesMap = ObjectMap<String, String>()
        Gdx.files.internal("game.properties").reader().use {
            PropertiesUtils.load(propertiesMap,it)
        }
        propertiesMap.toGameProperties()
    }


    private lateinit var audioManager : AudioManager
    private val preferences : Preferences by lazy {
        Gdx.app.getPreferences("treasure_hunter")
    }
    private val gamePreferences : GamePreferences by lazy {
        GamePreferences(preferences)
    }

    private val spriteBatch by lazy {
        SpriteBatch()
    }
    private val uiViewPort by lazy {  StretchViewport(640f, 360f) }
    private val stage by lazy { Stage(uiViewPort,spriteBatch) }

    private val assetHelper: AssetHelper by lazy { AssetHelper() }

    override fun create() {
        audioManager = AudioManager(
            if (gamePreferences.soundVolume != 0f) gamePreferences.soundVolume else gameProperties.soundVolume,
            if (gamePreferences.musicVolume != 0f) gamePreferences.musicVolume else gameProperties.musicVolume,
            gamePreferences.muteMusic,
            gamePreferences.muteSound
        ).also {
            GameEventDispatcher.registerListener(it)
        }
        Gdx.input.inputProcessor = InputMultiplexer()
        addScreen(LoadingScreen(assetHelper, spriteBatch, this, physicWorld,audioManager,stage, gameProperties,gamePreferences))
        setScreen<LoadingScreen>()
    }

    override fun render() {
        clearScreen(0f,0f,0f,0f)
        currentScreen.render(Gdx.graphics.deltaTime.coerceAtMost(1/30f))
    }

    override fun dispose() {
        super.dispose()
        audioManager.dispose()
        assetHelper.disposeSafely()
        spriteBatch.disposeSafely()
        physicWorld.disposeSafely()
        Gdx.input.inputProcessor = null
    }
}
