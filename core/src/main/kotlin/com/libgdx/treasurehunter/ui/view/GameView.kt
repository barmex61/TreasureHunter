package com.libgdx.treasurehunter.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.ui.model.GameModel
import com.libgdx.treasurehunter.ui.model.InventoryModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ktx.actors.alpha
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.container
import ktx.scene2d.image
import ktx.scene2d.stack
import ktx.scene2d.verticalGroup
import kotlin.reflect.KClass


class GameView(
    skin: Skin,
    gameModel: GameModel,
    inventoryModel: InventoryModel,
) : Table(skin), KTable{
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val playerLifeBar : Image
    private val enemyLifeBar : Image
    private val inventoryView : InventoryView = InventoryView(skin,inventoryModel).also {
        it.alpha = 0f
        it.touchable = Touchable.disabled
    }
    private val dialogView : DialogView = DialogView(skin, toggleDialogView = {
        this@GameView.toggleView(this::class)
    }).also {
        it.alpha = 0f
        it.touchable = Touchable.disabled
    }

    private var isInventoryVisible = false
    private var isDialogVisible = false
    init {
        setFillParent(true)
        verticalGroup {
            align(Align.topLeft)
            expand()
            padLeft(25.0f)
            padTop(25.0f)
            columnAlign(Align.topLeft)
            it.grow()
            container {
                align(Align.left)
                stack {
                    image("prefabs_15")
                    container {
                        padLeft(14.0f)
                        padBottom(2.0f)
                        this@GameView.playerLifeBar = image("life_bars_colors_1")
                    }
                }
            }
            container {
                align(Align.left)
                stack {
                    image("prefabs_17")
                    container {
                        padLeft(12.0f)
                        padBottom(1f)
                        image("life_bars_colors_2")
                    }
                }
            }
            container {
                align(Align.left)
                stack {
                    image("prefabs_18")
                    container {
                        padLeft(12.0f)
                        padBottom(1.0f)
                        image("life_bars_colors_3")
                    }
                }
            }
            container {
                align(Align.left)
                stack {
                    image("prefabs_19")
                    container {
                        padLeft(12.0f)
                        padBottom(1.0f)
                        image("life_bars_colors_4")
                    }
                }
            }
        }
        verticalGroup {
            align(Align.top)
            expand()
            fill()
            padTop(25.0f)
            it.grow()
            container {
                stack {
                    image("prefabs_16")
                    container {
                        padLeft(14.0f)
                        padBottom(2.0f)
                        this@GameView.enemyLifeBar = image("life_bars_colors_1")
                    }
                }
            }
        }
        add().grow()

        row()
        add().grow()
        add().grow()
        add().grow()

        row()
        add().grow()
        add().grow()
        verticalGroup {
            align(Align.bottomRight)
            expand()
            fill()
            padRight(25.0f)
            padBottom(25.0f)
            it.grow()
            container {
                prefSize(42f,42f)
                align(Align.bottomRight)
                image("inventory"){
                    onClick {
                        this@GameView.toggleView(InventoryView::class)
                    }
                }
            }
        }
        addActor(inventoryView)
        addActor(dialogView)
        coroutineScope.launch {
            launch {
                gameModel.playerLifeBarScale.collect { lifeBarScale ->
                    playerLifeBar += Actions.scaleTo(
                        lifeBarScale,
                        1f,
                        0.4f,
                        Interpolation.smooth
                    )
                }
            }
            launch {
                gameModel.enemyLifeBarScale.collect { lifeBarScale ->
                    enemyLifeBar += Actions.scaleTo(lifeBarScale, 1f, 0.4f, Interpolation.smooth)
                }
            }

        }
    }

    fun toggleView(viewClass : KClass<out Actor>) {
        val (actor,isVisible) = when(viewClass){
            InventoryView::class -> {
                isInventoryVisible = !isInventoryVisible
                Pair(inventoryView, isInventoryVisible)
            }
            DialogView::class -> {
                isDialogVisible = !isDialogVisible
                Pair(dialogView,isDialogVisible)
            }
            else -> throw IllegalArgumentException("Unsupported view class: $viewClass")
        }
        actor.clearActions()
        actor += if (isVisible){
            actor.touchable = Touchable.childrenOnly
            Actions.alpha(1f,0.5f)
        }else {
            actor.touchable = Touchable.disabled
            Actions.fadeOut(0.5f)
        }
    }
}

@Scene2dDsl
fun <S> KWidget<S>.gameView(
    gameModel: GameModel,
    inventoryModel: InventoryModel = InventoryModel().also { GameEventDispatcher.registerListener(it) },
    skin: Skin = Scene2DSkin.defaultSkin,
    init: GameView.(S) -> Unit = {},
): GameView = actor(GameView(skin, gameModel,inventoryModel),init)
