package com.libgdx.treasurehunter.ui.navigation

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.libgdx.treasurehunter.ui.model.GameModel
import com.libgdx.treasurehunter.ui.model.MenuModel
import com.libgdx.treasurehunter.ui.view.gameView
import com.libgdx.treasurehunter.ui.view.menuView
import com.libgdx.treasurehunter.ui.view.settingsView
import ktx.actors.alpha
import ktx.actors.plusAssign
import ktx.scene2d.actors
import com.badlogic.gdx.utils.Align
import com.libgdx.treasurehunter.ui.view.howToPlayView
import com.libgdx.treasurehunter.utils.GamePreferences

enum class ViewType{
    MENU,
    GAME,
    SETTINGS,
    HOW_TO_PLAY
}

enum class TransitionType {
    FADE,
    SLIDE_LEFT,
    SLIDE_RIGHT,
    SCALE_UP,
    SCALE_DOWN,
    ROTATE
}

class StageNavigator(
    val stage: Stage,
    val menuModel: MenuModel? = null,
    var gameModel: GameModel? = null,
    val gamePreferences: GamePreferences,
    val navigateToScreen : StageNavigator.(ViewType) -> Unit
){
    private var currentView: Actor? = null
    private var nextView: Actor? = null
    private var currentTransitionType: TransitionType = TransitionType.SLIDE_LEFT

    fun changeStageView(
        viewType: ViewType,
        transitionType: TransitionType = TransitionType.SLIDE_LEFT
    ){
        currentTransitionType = transitionType

        when(viewType){
            ViewType.MENU -> {
                menuModel?:return
                stage.actors {
                    menuView(menuModel, stageNavigator = this@StageNavigator).apply {
                        setupInitialState()
                        nextView = this
                    }
                }
            }
            ViewType.GAME -> {
                gameModel?:return
                stage.actors {
                    gameView(gameModel!!).apply {
                        setupInitialState()
                        nextView = this
                    }
                }
            }
            ViewType.SETTINGS -> {
                stage.actors {
                    settingsView(stageNavigator = this@StageNavigator, gamePreferences = this@StageNavigator.gamePreferences).apply {
                        setupInitialState()
                        nextView = this
                    }
                }
            }
            ViewType.HOW_TO_PLAY -> {
                stage.actors {
                    howToPlayView(stageNavigator = this@StageNavigator).apply {
                        setupInitialState()
                        nextView = this
                    }
                }
            }
        }

        applyTransition()
    }

    private fun Actor.setupInitialState() {
        when(currentTransitionType) {
            TransitionType.FADE -> {
                alpha = 0f
            }
            TransitionType.SLIDE_LEFT -> {
                alpha = 1f
                setPosition(stage.width, 0f)
            }
            TransitionType.SLIDE_RIGHT -> {
                alpha = 1f
                setPosition(-stage.width, 0f)
            }
            TransitionType.SCALE_UP -> {
                alpha = 1f
                setScale(0.1f)
                setOrigin(Align.center)
            }
            TransitionType.SCALE_DOWN -> {
                alpha = 1f
                setScale(2f)
                setOrigin(Align.center)
            }
            TransitionType.ROTATE -> {
                alpha = 1f
                setScale(0.5f)
                setRotation(180f)
                setOrigin(Align.center)
            }
        }
    }

    private fun applyTransition() {
        currentView?.let { current ->
            current += getExitAction()
        }

        nextView?.let { next ->
            next += getEnterAction()
        }

        currentView = nextView
        nextView = null
    }

    private fun getExitAction(): SequenceAction {
        return Actions.sequence(
            when(currentTransitionType) {
                TransitionType.FADE -> Actions.parallel(
                    Actions.alpha(0f, 0.5f)
                )
                TransitionType.SLIDE_LEFT -> Actions.parallel(
                    Actions.moveBy(-stage.width, 0f, 0.5f),
                    Actions.alpha(0f, 0.5f)
                )
                TransitionType.SLIDE_RIGHT -> Actions.parallel(
                    Actions.moveBy(stage.width, 0f, 0.5f),
                    Actions.alpha(0f, 0.5f)
                )
                TransitionType.SCALE_UP -> Actions.parallel(
                    Actions.scaleTo(2f, 2f, 0.5f),
                    Actions.alpha(0f, 0.5f)
                )
                TransitionType.SCALE_DOWN -> Actions.parallel(
                    Actions.scaleTo(0.1f, 0.1f, 0.5f),
                    Actions.alpha(0f, 0.5f)
                )
                TransitionType.ROTATE -> Actions.parallel(
                    Actions.rotateTo(360f, 0.5f),
                    Actions.scaleTo(0.1f, 0.1f, 0.5f),
                    Actions.alpha(0f, 0.5f)
                )
            },
            Actions.removeActor()
        )
    }

    private fun getEnterAction(): ParallelAction {
        return when(currentTransitionType) {
            TransitionType.FADE -> Actions.parallel(
                Actions.alpha(1f, 0.5f)
            )
            TransitionType.SLIDE_LEFT -> Actions.parallel(
                Actions.moveTo(0f, 0f, 0.5f),
                Actions.alpha(1f, 0.5f)
            )
            TransitionType.SLIDE_RIGHT -> Actions.parallel(
                Actions.moveTo(0f, 0f, 0.5f),
                Actions.alpha(1f, 0.5f)
            )
            TransitionType.SCALE_UP -> Actions.parallel(
                Actions.scaleTo(1f, 1f, 0.5f),
                Actions.alpha(1f, 0.5f)
            )
            TransitionType.SCALE_DOWN -> Actions.parallel(
                Actions.scaleTo(1f, 1f, 0.5f),
                Actions.alpha(1f, 0.5f)
            )
            TransitionType.ROTATE -> Actions.parallel(
                Actions.rotateTo(0f, 0.5f),
                Actions.scaleTo(1f, 1f, 0.5f),
                Actions.alpha(1f, 0.5f)
            )
        }
    }
}
