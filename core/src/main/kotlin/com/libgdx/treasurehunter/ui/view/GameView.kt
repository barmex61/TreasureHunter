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
                gdxError(throwable)
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
            expand()
            padLeft(15.0f)
            padTop(25.0f)
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
            expand()
            align(Align.top)
            columnAlign(Align.top)
            padTop(15f)
            it.grow()
            it.minSize(10f, -1f)
            it.maxSize(130f, -1f)
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
                gameModel.playerLife.collect { playerEntityLife ->
                    if (previousPlayerMaxLife != playerEntityLife.maxLife) {
                        updatePlayerStack(playerEntityLife.maxLife, playerEntityLife.entity)
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
                gameModel.enemyLife.collect { enemyEntityLife ->
                    updateEnemyStack(
                        enemyEntityLife.currentLife,
                        enemyEntityLife.maxLife,
                        enemyEntityLife.entity
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

    private fun updateEnemyStack(currentLife: Int, maxLife: Int, enemyEntity: Entity) {
        val enemyLifeStacks = enemyVerticalGroup.children.filterIsInstance<Stack>()
        val stackEntities: List<Entity> = enemyLifeStacks.map { stack ->
            stack.userObject as Entity
        }
        if (enemyEntity !in stackEntities || enemyLifeStacks.isEmpty()) {
            var lifeBarImage: Image? = null
            enemyVerticalGroup.addActor(stack {
                userObject = enemyEntity
                addActor(
                    this@GameView.barHorizontalGroup(
                        maxLife,
                        "pink_star_icon",
                        false
                    ) { img ->
                        lifeBarImage = img
                    })
            })
            lifeBarImage?.let { enemyLifeBarMap[enemyEntity] = it }
        }
        if (enemyEntity in stackEntities) {

            val index = stackEntities.indexOf(enemyEntity)
            val enemyStack = enemyLifeStacks[index]
            enemyLifeBarMap[enemyEntity]?.let { barImage ->
                val scale = currentLife / maxLife.toFloat()
                barImage.clearActions()
                enemyStack.clearActions()
                barImage += Actions.scaleTo(scale, 1f, 0.4f, Interpolation.smooth)
                enemyStack.addAction(
                    Actions.sequence(
                    Actions.delay(1.5f),
                    Actions.fadeOut(0.4f),
                    Actions.run {
                        enemyVerticalGroup.removeActor(enemyStack)
                        enemyLifeBarMap.remove(enemyEntity)
                    }
                ))
            }
        }
    }

    private fun updatePlayerStack(maxLife: Int, playerEntity: Entity) {
        playerLifeBarStack.apply {
            clearChildren()
            addActor(barHorizontalGroup(maxLife, "captain_clown_icon", true){img ->
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
            size(100f, 29f)
            prefSize(100f, 20f)
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
                image(entityIcon)
                stack {
                    horizontalGroup {
                        image(startBarImage)
                        (1..(length - 2).coerceAtMost(3)).forEach { index ->
                            image(midBarImage)
                        }
                        image(endBarImage)

                    }
                    container {
                        fillX()
                        val padL = when(barType){
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
