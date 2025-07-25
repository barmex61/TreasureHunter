package com.libgdx.treasurehunter.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.ui.model.GameModel
import com.libgdx.treasurehunter.ui.model.InventoryModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ktx.actors.alpha
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.app.gdxError
import ktx.scene2d.KContainer
import ktx.scene2d.KHorizontalGroup
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.container
import ktx.scene2d.horizontalGroup
import ktx.scene2d.image
import ktx.scene2d.stack
import ktx.scene2d.verticalGroup
import kotlin.reflect.KClass


class GameView(
    skin: Skin,
    gameModel: GameModel,
    inventoryModel: InventoryModel,
) : Table(skin), KTable {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            Gdx.app.postRunnable {
                gdxError(throwable)
            }
        }
    private var playerLifeBar: Image? = null
    private val enemyLifeBar: Image
    private val inventoryView: InventoryView = InventoryView(skin, inventoryModel).also {
        it.alpha = 0f
        it.touchable = Touchable.disabled
    }
    private val dialogView: DialogView = DialogView(skin, toggleDialogView = {
        this@GameView.toggleView(this::class)
    }).also {
        it.alpha = 0f
        it.touchable = Touchable.disabled
    }
    private val playerStack: Stack

    private var isInventoryVisible = false
    private var isDialogVisible = false

    private var previousPlayerMaxLife: Int = 0

    init {
        setFillParent(true)
        verticalGroup {
            align(Align.topLeft)
            expand()
            padLeft(15.0f)
            padTop(25.0f)
            columnAlign(Align.topLeft)
            it.grow()
            this@GameView.playerStack = stack {}
            stack {
                image("prefabs_17")
                container {
                    padLeft(12.0f)
                    padBottom(1f)
                    image("life_bars_colors_2")
                }
            }
            stack {
                image("prefabs_18")
                container {
                    padLeft(12.0f)
                    padBottom(1.0f)
                    image("life_bars_colors_3")
                }
            }
            stack {
                image("prefabs_19")
                container {
                    padLeft(12.0f)
                    padBottom(1.0f)
                    image("life_bars_colors_4")
                }
            }
        }
        verticalGroup {
            expand()
            align(Align.top)
            columnAlign(Align.top)
            padTop(15f)
            it.grow()
            it.minSize(10f,-1f)
            it.maxSize(130f,-1f)
            stack {
                container {
                    setSize(50f,-1f)
                    prefSize(50f,-1f)
                    horizontalGroup {
                        image("life_bars_big_bars_1")
                        image("life_bars_big_bars_3")
                        image("life_bars_big_bars_3")
                        image("life_bars_big_bars_3")
                        image("life_bars_big_bars_3")
                        image("life_bars_big_bars_4")
                        image("life_bars_big_bars_4")
                        image("life_bars_big_bars_4")
                    }
                }

                container {
                    fillX()
                    padLeft(17f)
                    padRight(4f)
                    padBottom(1f)
                    this@GameView.enemyLifeBar = image("life_bars_colors_1")
                }
            }
            stack {
                horizontalGroup {
                    image("life_bars_medium_bars_3")
                    image("life_bars_medium_bars_4")
                    image("life_bars_medium_bars_5")
                }
                container {
                    fillX()
                    padLeft(29f)
                    padRight(17f)
                    image("life_bars_colors_1")
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
                prefSize(42f, 42f)
                align(Align.bottomRight)
                image("inventory") {
                    onClick {
                        this@GameView.toggleView(InventoryView::class)
                    }
                }
            }
        }
        addActor(inventoryView)
        addActor(dialogView)

        coroutineScope.launch(coroutineExceptionHandler) {
            launch {
                gameModel.playerLife.collect { playerLife ->
                    if (previousPlayerMaxLife != playerLife.maxLife) {
                        updatePlayerStack(playerLife.maxLife)
                        previousPlayerMaxLife = playerLife.maxLife
                    }
                    playerLifeBar?.let {
                        it += Actions.scaleTo(
                            playerLife.currentLife / playerLife.maxLife.toFloat(),
                            1f,
                            0.4f,
                            Interpolation.smooth
                        )
                    }
                }
            }

            /*
            launch {
                gameModel.enemyLifeBarScale.collect { lifeBarScale ->
                    enemyLifeBar += Actions.scaleTo(lifeBarScale, 1f, 0.4f, Interpolation.smooth)
                }
            } */

        }
    }

    fun toggleView(viewClass: KClass<out Actor>) {
        val (actor, isVisible) = when (viewClass) {
            InventoryView::class -> {
                isInventoryVisible = !isInventoryVisible
                Pair(inventoryView, isInventoryVisible)
            }

            DialogView::class -> {
                isDialogVisible = !isDialogVisible
                Pair(dialogView, isDialogVisible)
            }

            else -> throw IllegalArgumentException("Unsupported view class: $viewClass")
        }
        actor.clearActions()
        actor += if (isVisible) {
            actor.touchable = Touchable.childrenOnly
            Actions.alpha(1f, 0.5f)
        } else {
            actor.touchable = Touchable.disabled
            Actions.fadeOut(0.5f)
        }
    }

    private fun updatePlayerStack(maxLife: Int) {
        playerStack.clearChildren()
        playerStack.addActor(barHorizontalGroup(maxLife, "captain_clown_icon"))
    }

    private fun barHorizontalGroup(length: Int, entityIcon: String): KContainer<Actor> =
        container {
            size(100f,29f)
            prefSize(100f,20f)
            horizontalGroup {
                val barType = when (length) {
                    in Int.MIN_VALUE..4 -> BarType.MEDIUM
                    in 5..Int.MAX_VALUE -> BarType.BIG
                    else -> gdxError("There is no range for $length to create bar")
                }
                val (startBarImage, midBarImage, endBarImage) = when (barType) {

                    BarType.MEDIUM -> Triple(
                        "life_bars_medium_bars_6",
                        "life_bars_medium_bars_4",
                        "life_bars_medium_bars_5"
                    )

                    BarType.BIG -> Triple(
                        "life_bars_big_bars_2",
                        "life_bars_big_bars_3",
                        "life_bars_big_bars_4"
                    )
                }
                image(entityIcon)
                stack {
                    horizontalGroup {
                        image(startBarImage)
                        (1..(length - 2).coerceAtMost(3)).forEach { index ->
                            image(midBarImage)
                        }
                        image(endBarImage)
                        setScale(0.1f,0.1f)

                    }
                    container {
                        fillX()
                        padLeft(14.0f)
                        padBottom(2.0f)
                        this@GameView.playerLifeBar = image("life_bars_colors_1")
                    }
                }

            }
        }

}

enum class BarType {
    MEDIUM, BIG
}

@Scene2dDsl
fun <S> KWidget<S>.gameView(
    gameModel: GameModel,
    inventoryModel: InventoryModel = InventoryModel().also { GameEventDispatcher.registerListener(it) },
    skin: Skin = Scene2DSkin.defaultSkin,
    init: GameView.(S) -> Unit = {},
): GameView = actor(GameView(skin, gameModel, inventoryModel), init)
