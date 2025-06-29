package com.libgdx.treasurehunter.ui.view

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.libgdx.treasurehunter.enums.SoundAsset
import com.libgdx.treasurehunter.ui.model.MenuModel
import com.libgdx.treasurehunter.ui.navigation.StageNavigator
import com.libgdx.treasurehunter.ui.navigation.ViewType
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import ktx.actors.onClick
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.container
import ktx.scene2d.horizontalGroup
import ktx.scene2d.image
import ktx.scene2d.stack
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup
import javax.swing.text.View


class MenuView(
    skin : Skin,
    menuModel: MenuModel,
    stageNavigator: StageNavigator
): Table(skin),KTable {
    private val mainMenuText = "MAIN MENU"
    init {
        setFillParent(true)

        stack{
            it.prefSize(200f)
            image( "prefabs_3_ninepatch")
            verticalGroup{
                expand()
                fill()
                padLeft(15.0f)
                padRight(15.0f)
                padTop(28.0f)
                padBottom(28.0f)
                columnAlign(Align.top)
                container{
                    size(120f,50f)
                    stack{
                        image("prefabs_9"){
                            setScaling(Scaling.stretch)
                        }
                        horizontalGroup{
                            align(Align.center)
                            this@MenuView.mainMenuText.forEach {
                                image("big_text_${if (it.isWhitespace()) "space" else it.lowercase()}")
                            }
                        }
                    }
                }

                verticalGroup{
                    expand()
                    fill()
                    textButton("Play"){
                        onClick {
                            GameEventDispatcher.fireEvent(GameEvent.PlaySoundEvent(SoundAsset.BUTTON_CLICK))
                            with(stageNavigator) {
                                navigateToScreen(ViewType.GAME)
                            }
                            true
                        }
                    }
                    textButton("Settings"){
                        onClick {
                            GameEventDispatcher.fireEvent(GameEvent.PlaySoundEvent(SoundAsset.BUTTON_CLICK))

                            stageNavigator.changeStageView(
                                viewType = ViewType.SETTINGS
                            )
                            true
                        }
                    }
                }
            }
        }
    }
}

@Scene2dDsl
fun <S> KWidget<S>.menuView(
    menuModel: MenuModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    stageNavigator: StageNavigator
): MenuView = actor(MenuView(skin, menuModel,stageNavigator)) {}
