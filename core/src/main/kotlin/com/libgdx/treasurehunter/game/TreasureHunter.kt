package com.libgdx.treasurehunter.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.World
import com.libgdx.treasurehunter.audio.AudioManager
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.game.screens.LoadingScreen
import com.libgdx.treasurehunter.utils.Constants
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
    private val audioManager by lazy {
        AudioManager()
    }.also {
        GameEventDispatcher.registerListener(it.value)
    }

    private val spriteBatch by lazy {
        SpriteBatch()
    }

    private val assetHelper: AssetHelper by lazy { AssetHelper() }

    override fun create() {
        Gdx.input.inputProcessor = InputMultiplexer()
        addScreen(LoadingScreen(assetHelper, spriteBatch, this, physicWorld,audioManager))
        setScreen<LoadingScreen>()
    }

    override fun render() {
        clearScreen(0f,0f,0f,0f)
        currentScreen.render(Gdx.graphics.deltaTime.coerceAtMost(1/30f))
    }

    override fun dispose() {
        super.dispose()
        assetHelper.disposeSafely()
        spriteBatch.disposeSafely()
        physicWorld.disposeSafely()
        Gdx.input.inputProcessor = null
    }
}
