package com.libgdx.treasurehunter.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.libgdx.treasurehunter.enums.SoundAsset
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.ui.navigation.StageNavigator
import com.libgdx.treasurehunter.ui.navigation.TransitionType
import com.libgdx.treasurehunter.ui.navigation.ViewType
import ktx.actors.onClick
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.container
import ktx.scene2d.horizontalGroup
import ktx.scene2d.image
import ktx.scene2d.imageButton
import ktx.scene2d.stack
import ktx.scene2d.textArea
import ktx.scene2d.textField
import ktx.scene2d.verticalGroup


class HowToPlayView(
    skin: Skin,
    stageNavigator: StageNavigator,
) : KTable, Table(skin) {
    init {
        setFillParent(true)

        stack {

            image("prefabs_3_ninepatch")
            container {
                align(Align.topLeft)
                imageButton {
                    onClick {
                        GameEventDispatcher.fireEvent(GameEvent.PlaySoundEvent(SoundAsset.BUTTON_CLICK))
                        stageNavigator.changeStageView(ViewType.MENU, TransitionType.SLIDE_RIGHT)
                        false
                    }
                }
            }
            verticalGroup {
                align(Align.top)
                padLeft(15f)
                padTop(20f)
                padBottom(20f)
                space(12f)
                horizontalGroup {
                    padLeft(20f)
                    padRight(20f)
                    verticalGroup {
                        space(5f)
                        image("big_text_w")
                        horizontalGroup {
                            space(5f)
                            image("big_text_a")
                            image("big_text_s")
                            image("big_text_d")
                        }
                    }
                    textField("Movement Keys") {
                        alignment = Align.center
                    }
                }
                addActor(this@HowToPlayView.customHorizontalGroup(listOf(
                        "big_text_s",
                        "big_text_p",
                        "big_text_a",
                        "big_text_c",
                        "big_text_e"
                    ),"Jump Key",0f)
                )
                addActor(this@HowToPlayView.customHorizontalGroup(listOf(
                        "big_text_1",
                        "big_text_2",
                        "big_text_3",
                    ),"Attack Keys",9f)
                )
                addActor(this@HowToPlayView.customHorizontalGroup(listOf(
                        "big_text_p",

                    ),"Throw Item Key",0f, Align.right)
                )
                addActor(this@HowToPlayView.customHorizontalGroup(listOf(
                        "big_text_i",
                    ),"Open Inventory Key",0f, Align.right)
                )
            }
        }
    }

    private fun customHorizontalGroup(imagePaths: List<String>, keyPath: String,space: Float,textAlign : Int = Align.center) = horizontalGroup {
        padLeft(20f)
        padRight(20f)
        if (imagePaths.size > 1) {
            horizontalGroup {
                space(space)
                imagePaths.forEach { imagePath ->
                    image(imagePath)
                }
            }
        }else{
            image(imagePaths.first())
        }
        textField(keyPath) {
            alignment = textAlign
        }
    }

}


@Scene2dDsl
fun <S> KWidget<S>.howToPlayView(
    skin: Skin = Scene2DSkin.defaultSkin,
    stageNavigator: StageNavigator,
    init: HowToPlayView.(S) -> Unit = {},
): HowToPlayView = actor(HowToPlayView(skin, stageNavigator), init)
