package com.libgdx.treasurehunter.ui.view

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.ui.model.GameModel
import com.libgdx.treasurehunter.ui.model.InventoryModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ktx.actors.alpha
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.container
import ktx.scene2d.image
import ktx.actors.plusAssign
import ktx.scene2d.stack


class GameView(
    skin: Skin,
    gameModel : GameModel,
    inventoryModel: InventoryModel
) : Table(skin), KTable{
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val playerLifeBar : Image
    private val enemyLifeBar : Image
    private val inventoryView : InventoryView = InventoryView(skin,inventoryModel).also { it.alpha = 0f }
    private var isInventoryVisible = false
    init {
        setFillParent(true)

        stack{
            image("prefabs_15")
            container{
                padLeft(14.0f).padBottom(2.0f)
                this@GameView.playerLifeBar = image("life_bars_colors_1")
            }
            it.padLeft(10.0f).padTop(10.0f).expand().align(Align.topLeft)
        }
        stack{
            image("prefabs_16")
            container{
                padLeft(14.0f).padBottom(2.0f)
                this@GameView.enemyLifeBar = image("life_bars_colors_1")
            }
            it.padTop(10.0f).padRight(50f).expand().align(Align.top)
        }
        add().expand()
        row()
        add().expand()
        add().expand()
        add().expand()
        row()
        add().expand()
        add().expand()
        add().expand()
        addActor(inventoryView)
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

    fun toggleInventoryView() {
        isInventoryVisible = !isInventoryVisible
        inventoryView.clearActions()
        inventoryView += if (isInventoryVisible){
            Actions.fadeIn(1f)
        }else {
            Actions.fadeOut(1f)
        }
    }
}

@Scene2dDsl
fun <S> KWidget<S>.gameView(
    gameModel: GameModel,
    inventoryModel: InventoryModel = InventoryModel().also { GameEventDispatcher.registerListener(it) },
    skin: Skin = Scene2DSkin.defaultSkin,
    init : GameView.(S) -> Unit = {}
): GameView = actor(GameView(skin, gameModel,inventoryModel),init)
