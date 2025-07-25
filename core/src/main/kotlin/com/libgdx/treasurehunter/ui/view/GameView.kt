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
import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.ui.model.GameModel
import com.libgdx.treasurehunter.ui.model.InventoryModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import ktx.actors.alpha
import ktx.actors.onClick
import ktx.actors.plusAssign
import ktx.app.gdxError
import ktx.scene2d.KContainer
import ktx.scene2d.KTable
import ktx.scene2d.KVerticalGroup
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
                gdxError("throwable $throwable message ${throwable.message} cause ${throwable.cause} localized ${throwable.localizedMessage}")
            }
        }
    private var playerLifeBar: Image? = null
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
    private val playerLifeBarStack: Stack

    private val enemyVerticalGroup: KVerticalGroup

    private var isInventoryVisible = false
    private var isDialogVisible = false

    private var previousPlayerMaxLife: Int = 0

    private val enemyLifeBarMap = mutableMapOf<Entity, Image>()

    init {
        setFillParent(true)
        verticalGroup {
            align(Align.topLeft)
            padLeft(15.0f)
            padTop(15.0f)
            columnAlign(Align.topLeft)
            it.grow()
            this@GameView.playerLifeBarStack = stack {}
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
        this.enemyVerticalGroup = verticalGroup {
            align(Align.topLeft)
            columnAlign(Align.top)
            padTop(15f)
            it.grow()

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
                gameModel.playerLife.filter { it.entity != Entity.NONE }
                    .collect { playerEntityLife ->
                        if (previousPlayerMaxLife != playerEntityLife.maxLife) {
                            updatePlayerStack(
                                playerEntityLife.maxLife,
                                playerEntityLife.entity,
                                playerEntityLife.entityIcon
                            )
                            previousPlayerMaxLife = playerEntityLife.maxLife
                        }
                        playerLifeBar?.let {
                            it += Actions.scaleTo(
                                playerEntityLife.currentLife / playerEntityLife.maxLife.toFloat(),
                                1f,
                                0.4f,
                                Interpolation.smooth
                            )
                        }
                    }
            }


            launch {
                gameModel.enemyLife.filter { it.entity != Entity.NONE }.collect { enemyEntityLife ->
                    updateEnemyStack(
                        enemyEntityLife.currentLife,
                        enemyEntityLife.maxLife,
                        enemyEntityLife.entity,
                        enemyEntityLife.entityIcon
                    )
                }
            }

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

    private fun updateEnemyStack(
        currentLife: Int,
        maxLife: Int,
        enemyEntity: Entity,
        enemyIcon: String,
    ) {
        val enemyLifeStacks = enemyVerticalGroup.children.filterIsInstance<Stack>()
        val stackEntities: List<Entity> =
            enemyLifeStacks.map { stack -> stack.userObject as Entity }
        var enemyStack: Stack?
        var lifeBarImage: Image? = null

        if (enemyEntity !in stackEntities || enemyLifeStacks.isEmpty()) {
            val newStack = stack {
                userObject = enemyEntity
                addActor(
                    this@GameView.barHorizontalGroup(
                        maxLife,
                        enemyIcon,
                        false
                    ) { img ->
                        lifeBarImage = img
                    })
            }
            enemyVerticalGroup.addActor(newStack)
            enemyStack = newStack
            lifeBarImage?.let { enemyLifeBarMap[enemyEntity] = it }
        } else {
            val index = stackEntities.indexOf(enemyEntity)
            enemyStack = enemyLifeStacks[index]
            lifeBarImage = enemyLifeBarMap[enemyEntity]
        }

        lifeBarImage?.let { barImage ->
            val scale = currentLife / maxLife.toFloat()
            barImage.clearActions()
            enemyStack.clearActions()
            barImage += Actions.scaleTo(scale, 1f, 0.4f, Interpolation.smooth)
            enemyStack.addAction(
                Actions.sequence(
                    Actions.fadeIn(0.25f),
                    Actions.delay(1.5f),
                    Actions.fadeOut(0.4f),
                    Actions.run {
                        enemyVerticalGroup.removeActor(enemyStack)
                        enemyLifeBarMap.remove(enemyEntity)
                    }
                )
            )
        }
    }

    private fun updatePlayerStack(maxLife: Int, playerEntity: Entity, playerIcon: String) {
        playerLifeBarStack.apply {
            clearChildren()

            addActor(barHorizontalGroup(maxLife, playerIcon, true) { img ->
                if (this@GameView.playerLifeBar == null) this@GameView.playerLifeBar = img
            })
            userObject = playerEntity
        }
    }


    private fun barHorizontalGroup(
        length: Int,
        entityIcon: String,
        isPlayer: Boolean,
        onLifeBarImageCreated: ((Image) -> Unit)? = null,
    ): KContainer<Actor> =
        container {
            horizontalGroup {
                space(3f)
                val barType = when (length) {
                    in Int.MIN_VALUE..4 -> BarType.MEDIUM
                    in 5..Int.MAX_VALUE -> BarType.BIG
                    else -> gdxError("There is no range for $length to create bar")
                }
                val (startBarImage, midBarImage, endBarImage) = when (barType) {

                    BarType.MEDIUM -> Triple(
                        if (isPlayer) "life_bars_medium_bars_6" else "life_bars_medium_bars_7",
                        "life_bars_medium_bars_4",
                        "life_bars_medium_bars_5"
                    )

                    BarType.BIG -> Triple(
                        if (isPlayer) "life_bars_big_bars_1" else "life_bars_big_bars_2",
                        "life_bars_big_bars_3",
                        "life_bars_big_bars_4"
                    )
                }
                if (entityIcon.isNotBlank()) image(entityIcon)
                stack {
                    horizontalGroup {
                        image(startBarImage)
                        (1..(length - 2).coerceAtMost(if (isPlayer) 3 else 5)).forEach { index ->
                            image(midBarImage)
                        }
                        image(endBarImage)

                    }
                    container {
                        fillX()
                        val padL = when (barType) {
                            BarType.MEDIUM -> 14f
                            BarType.BIG -> 16f
                        }
                        padLeft(padL)
                        padRight(2.2f)
                        padBottom(1f)
                        val lifeBarImage = image("life_bars_colors_1")
                        onLifeBarImageCreated?.invoke(lifeBarImage)
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
