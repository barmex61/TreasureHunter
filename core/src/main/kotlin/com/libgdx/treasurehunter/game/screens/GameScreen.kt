package com.libgdx.treasurehunter.game.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.configureWorld
import com.libgdx.treasurehunter.ecs.systems.AnimationSystem
import com.libgdx.treasurehunter.ecs.systems.AttackSystem
import com.libgdx.treasurehunter.ecs.systems.BlinkSystem
import com.libgdx.treasurehunter.ecs.systems.CameraSystem
import com.libgdx.treasurehunter.ecs.systems.DamageSystem
import com.libgdx.treasurehunter.ecs.systems.DebugSystem
import com.libgdx.treasurehunter.ecs.systems.FlashSystem
import com.libgdx.treasurehunter.ecs.systems.GlProfilerSystem
import com.libgdx.treasurehunter.ecs.systems.InvulnarableSystem
import com.libgdx.treasurehunter.ecs.systems.JumpSystem
import com.libgdx.treasurehunter.ecs.systems.MoveSystem
import com.libgdx.treasurehunter.ecs.systems.PhysicSystem
import com.libgdx.treasurehunter.ecs.systems.RenderSystem
import com.libgdx.treasurehunter.ecs.systems.StateSystem
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.MapAssets
import com.libgdx.treasurehunter.enums.ShaderEffect
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.event.GameEventDispatcher.fireEvent
import com.libgdx.treasurehunter.event.GameEventListener
import com.libgdx.treasurehunter.game.inputMultiplexer
import com.libgdx.treasurehunter.input.KeyboardInputProcessor
import com.libgdx.treasurehunter.tiled.TiledMapService
import com.libgdx.treasurehunter.utils.GameObject
import ktx.app.KtxScreen
import ktx.app.gdxError
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.tiled.propertyOrNull

class GameScreen(private val spriteBatch: SpriteBatch,assetHelper: AssetHelper,private val physicWorld: com.badlogic.gdx.physics.box2d.World) : KtxScreen {


    private val gameViewPort by lazy { StretchViewport(32f, 18f) }

    private val gameCamera by lazy {
        (gameViewPort.camera as OrthographicCamera).apply {
            position.set(16f, 9f, 0f)
            update()
        }
    }

    private val world: World = configureWorld {
        injectables {
            add(assetHelper)
            add(spriteBatch)
            add(gameCamera)
            add(gameViewPort)
            add(physicWorld)
        }
        systems {
            add(StateSystem())
            add(MoveSystem())
            add(DamageSystem())
            add(FlashSystem())
            add(BlinkSystem())
            add(InvulnarableSystem())
            add(AttackSystem())
            add(JumpSystem())
            add(PhysicSystem())
            add(AnimationSystem())
            add(CameraSystem())
            add(GlProfilerSystem())
            add(RenderSystem())
            add(DebugSystem())

        }
    }

    private val tiledMapService by lazy { TiledMapService(physicWorld,world,assetHelper) }
    private val keyboardInputProcessor: KeyboardInputProcessor = KeyboardInputProcessor(world)

    init {
        registerGameEventListeners()
        fireEvent(GameEvent.MapChangeEvent(assetHelper[MapAssets.TUTORIAL]))
    }

    override fun show() {
        inputMultiplexer.addProcessor(keyboardInputProcessor)
        registerGameEventListeners()
    }

    override fun render(delta: Float) {
        // --- FOR DEBUG PURPOSES ---
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_5)) {
            println("Numpad 5 pressed")
            world.system<RenderSystem>().setShaderEffect(ShaderEffect.BURN_EFFECT)
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_6)) {
            println("Numpad 6 pressed")
            world.system<RenderSystem>().setShaderEffect(ShaderEffect.HEAL_EFFECT)
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_7)) {
            println("Numpad 7 pressed")
            world.system<RenderSystem>().setShaderEffect(ShaderEffect.FREEZE_EFFECT)
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8)) {
            println("Numpad 8 pressed")
            world.system<RenderSystem>().setShaderEffect(ShaderEffect.POISON_EFFECT)
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_9)) {
            println("Numpad 9 pressed")
            world.system<RenderSystem>().setShaderEffect(ShaderEffect.NORMAL)
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_4)) {
            println("Numpad 4 pressed")
            world.system<RenderSystem>().setShaderEffect(ShaderEffect.INVISIBLE_EFFECT)
        }else if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_0)) {
            println("Numpad 0 pressed")
            world.system<RenderSystem>().setShaderEffectFromColorSettings()
        }
        // ------------------
        world.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        gameViewPort.update(width, height, true)
    }

    override fun hide() {
        inputMultiplexer.removeProcessor(keyboardInputProcessor)
        unregisterGameEventListeners()
    }

    override fun dispose() {
        hide()
        tiledMapService.dispose()
        world.dispose()
    }

    private fun registerGameEventListeners() {
        GameEventDispatcher.registerListener(tiledMapService)
        world.systems.filterIsInstance<GameEventListener>().forEach { gameEventListener ->
            GameEventDispatcher.registerListener(gameEventListener)
        }
    }

    private fun unregisterGameEventListeners() {
        GameEventDispatcher.unRegisterListener(tiledMapService)
        world.systems.filterIsInstance<GameEventListener>().forEach { gameEventListener ->
            GameEventDispatcher.unRegisterListener(gameEventListener)
        }
    }


}
