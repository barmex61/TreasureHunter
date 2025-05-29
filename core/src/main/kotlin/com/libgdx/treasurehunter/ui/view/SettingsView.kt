package com.libgdx.treasurehunter.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.libgdx.treasurehunter.ui.navigation.StageNavigator
import com.libgdx.treasurehunter.ui.navigation.TransitionType
import com.libgdx.treasurehunter.ui.navigation.ViewType
import ktx.actors.onClick
import ktx.scene2d.KGroup
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.container
import ktx.scene2d.horizontalGroup
import ktx.scene2d.image
import ktx.scene2d.imageButton
import ktx.scene2d.imageTextButton
import ktx.scene2d.label
import ktx.scene2d.slider
import ktx.scene2d.stack
import ktx.scene2d.verticalGroup


class SettingsView(
    skin : Skin,
    stageNavigator: StageNavigator
): KTable , Table(skin) {
 init {

     setFillParent(true)
     stack{
         it.maxWidth(130f).prefWidth(70f).prefHeight(150f)
         image("prefabs_3_ninepatch")
         container{
             align(Align.topLeft)
             imageButton{
                 onClick {
                     stageNavigator.changeStageView(ViewType.MENU, TransitionType.SLIDE_RIGHT)
                     true
                 }
             }
         }
         verticalGroup{
             align(Align.center)
             stack{
                 image("prefabs_8"){
                     setScaling(Scaling.none)
                 }
                 verticalGroup{
                     align(Align.center)
                     columnAlign(Align.left)
                     space(4f)
                     imageTextButton("MUSIC")
                     imageTextButton("SFX")
                 }
             }
             container{
                 minWidth(70f)
                 maxHeight(25f)
                 label("VOLUME") {
                     setAlignment(Align.center)
                 }
             }
             container{
                 maxWidth(100f)
                 slider(min = 0f, max = 100f, step = 1f, vertical = false)
             }
         }
     }
     row()
     stack{
         it.prefHeight(50f)
         image("prefabs_3_ninepatch")
         horizontalGroup{
             align(Align.center)
             image("big_text_s")
             image("big_text_e")
             image("big_text_t")
             image("big_text_t")
             image("big_text_i")
             image("big_text_n")
             image("big_text_g")
             image("big_text_s")
         }
     }
 }


}


@Scene2dDsl
fun <S> KWidget<S>.settingsView(
    skin: Skin = Scene2DSkin.defaultSkin,
    stageNavigator: StageNavigator,
    init: SettingsView.(S) -> Unit = {}
): SettingsView = actor(SettingsView(skin,stageNavigator),init)
