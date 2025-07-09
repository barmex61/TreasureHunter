package com.libgdx.treasurehunter.game.screens

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.configureWorld
import com.libgdx.treasurehunter.audio.AudioManager
import com.libgdx.treasurehunter.ecs.systems.AiSystem
import com.libgdx.treasurehunter.ecs.systems.AnimationSystem
import com.libgdx.treasurehunter.ecs.systems.AttackMetaSystem
import com.libgdx.treasurehunter.ecs.systems.AttackSystem
import com.libgdx.treasurehunter.ecs.systems.BlinkSystem
import com.libgdx.treasurehunter.ecs.systems.CameraSystem
import com.libgdx.treasurehunter.ecs.systems.DamageSystem
import com.libgdx.treasurehunter.ecs.systems.DebugSystem
import com.libgdx.treasurehunter.ecs.systems.FlashSystem
import com.libgdx.treasurehunter.ecs.systems.GlProfilerSystem
import com.libgdx.treasurehunter.ecs.systems.InvulnarableSystem
import com.libgdx.treasurehunter.ecs.systems.ItemSystem
import com.libgdx.treasurehunter.ecs.systems.JumpSystem
import com.libgdx.treasurehunter.ecs.systems.MarkSystem
import com.libgdx.treasurehunter.ecs.systems.MoveSystem
import com.libgdx.treasurehunter.ecs.systems.ParticleSystem
import com.libgdx.treasurehunter.ecs.systems.PhysicSystem
import com.libgdx.treasurehunter.ecs.systems.RemoveSystem
import com.libgdx.treasurehunter.ecs.systems.RenderSystem
import com.libgdx.treasurehunter.ecs.systems.StateSystem
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.MapAssets
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.event.GameEventDispatcher.fireEvent
import com.libgdx.treasurehunter.event.GameEventListener
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.game.inputMultiplexer
import com.libgdx.treasurehunter.input.KeyboardInputProcessor
import com.libgdx.treasurehunter.tiled.TiledMapService
import com.libgdx.treasurehunter.ui.model.GameModel
import com.libgdx.treasurehunter.ui.navigation.StageNavigator
import com.libgdx.treasurehunter.ui.navigation.ViewType
import com.libgdx.treasurehunter.utils.GamePreferences
import com.libgdx.treasurehunter.utils.GameProperties
import ktx.app.KtxScreen

class GameScreen(
    private val spriteBatch: SpriteBatch,
    private val assetHelper: AssetHelper,
    private val physicWorld: PhysicWorld,
    private val audioManager: AudioManager,
    private val stage: Stage,
    private val stageNavigator: StageNavigator,
    private val gameProperties: GameProperties,
    private val gamePreferences: GamePreferences
    ) : KtxScreen {


    private val gameViewPort by lazy { StretchViewport(16f, 9f) }

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
            add(stage)
            add(gameCamera)
            add("gameViewPort",gameViewPort)
            add(physicWorld)
            add("uiViewPort",stage.viewport)
        }
        systems {
            add(StateSystem())
            add(MoveSystem())
            add(DamageSystem())
            add(FlashSystem())
            add(BlinkSystem())
            add(InvulnarableSystem())
            add(AttackSystem())
            add(AttackMetaSystem())
            add(AnimationSystem())
            add(MarkSystem())
            add(ItemSystem())
            add(JumpSystem())
            add(ParticleSystem())
            add(AiSystem())
            add(PhysicSystem())
            add(RemoveSystem())
            add(CameraSystem())
            add(GlProfilerSystem())
            add(RenderSystem())
            add(DebugSystem())

        }
    }
    private val gameModel = GameModel(world).also {
        stageNavigator.gameModel = it
    }
    private val tiledMapService by lazy { TiledMapService(physicWorld,world,assetHelper) }
    private val keyboardInputProcessor: KeyboardInputProcessor = KeyboardInputProcessor(world)

    init {
        registerGameEventListeners()
    }

    override fun show() {
        inputMultiplexer.addProcessor(keyboardInputProcessor)
        registerGameEventListeners()
        fireEvent(GameEvent.MapChangeEvent(assetHelper[MapAssets.MAP1]))
        stageNavigator.changeStageView(ViewType.GAME)
    }

    override fun render(delta: Float) {
        world.update(delta.coerceAtMost(0.25f))
        GdxAI.getTimepiece().update(delta.coerceAtMost(0.25f))
    }

    override fun resize(width: Int, height: Int) {
        gameViewPort.update(width, height, true)
        stage.viewport.update(width,height,true)
    }

    override fun hide() {
        inputMultiplexer.removeProcessor(keyboardInputProcessor)
        unregisterGameEventListeners()
        stage.clear()
    }

    override fun dispose() {
        hide()
        tiledMapService.dispose()
        world.dispose()
    }

    private fun registerGameEventListeners() {
        GameEventDispatcher.registerListener(tiledMapService)
        GameEventDispatcher.registerListener(gameModel)
        world.systems.filterIsInstance<GameEventListener>().forEach { gameEventListener ->
            GameEventDispatcher.registerListener(gameEventListener)
        }
    }

    private fun unregisterGameEventListeners() {
        GameEventDispatcher.unRegisterListener(tiledMapService)
        GameEventDispatcher.unRegisterListener(gameModel)
        world.systems.filterIsInstance<GameEventListener>().forEach { gameEventListener ->
            GameEventDispatcher.unRegisterListener(gameEventListener)
        }
    }


}
