package com.libgdx.treasurehunter.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.libgdx.treasurehunter.event.GameEvent.AudioChangeEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.ui.navigation.StageNavigator
import com.libgdx.treasurehunter.ui.navigation.TransitionType
import com.libgdx.treasurehunter.ui.navigation.ViewType
import com.libgdx.treasurehunter.utils.GamePreferences
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
    stageNavigator: StageNavigator,
    gamePreferences: GamePreferences
): KTable , Table(skin) {
    private var musicSlider : Slider ? = null
    private var sfxSlider : Slider ? = null
    private var musicButton : ImageTextButton ? = null
    private var sfxButton : ImageTextButton ? = null
 init {

     setFillParent(true)
     stack{
         it.prefSize(190f,170f)
         image("prefabs_3_ninepatch"){
             setScaling(Scaling.stretch)
         }
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
                     setScaling(Scaling.stretch)
                 }
                 verticalGroup{
                     align(Align.center)
                     columnAlign(Align.center)
                     space(5f)
                     pad(15f,10f,15f,10f)
                     this@SettingsView.musicButton = imageTextButton("MUSIC"){
                            this.isChecked = gamePreferences.muteMusic
                         onClick {
                                gamePreferences.storeMuteMusic(!this.isChecked)
                             if (this.isChecked){
                                 this@SettingsView.musicSlider?.value = 0f
                             }else{
                                 this@SettingsView.musicSlider?.value = 50f
                             }
                         }
                     }
                     this@SettingsView.musicSlider = slider(min = 0f, max = 100f, step = 1f, vertical = false){
                         this.value = gamePreferences.musicVolume * 100f
                         addListener { event ->
                             gamePreferences.storeMusicVolume(this.value / 100f)
                             if (this.value > 0f){
                                 this@SettingsView.musicButton?.isChecked = false
                                 gamePreferences.storeMuteMusic(false)
                             }else{
                                 this@SettingsView.musicButton?.isChecked = true
                                 gamePreferences.storeMuteMusic(true)
                             }
                             true
                         }
                     }
                     this@SettingsView.sfxButton = imageTextButton("SFX"){
                         this.isChecked = gamePreferences.muteSound
                         onClick {

                             gamePreferences.storeMuteSound(this.isChecked)
                             if (this.isChecked){
                                 this@SettingsView.sfxSlider?.value = 0f
                             }else{
                                 this@SettingsView.sfxSlider?.value = 50f
                             }
                         }
                     }
                     this@SettingsView.sfxSlider = slider(min = 0f, max = 100f, step = 1f, vertical = false){
                         this.value = gamePreferences.soundVolume * 100f
                         addListener { event ->
                             gamePreferences.storeSoundVolume(this.value / 100f)
                             if (this.value > 0f){
                                 this@SettingsView.sfxButton?.isChecked = false
                                 gamePreferences.storeMuteSound(false)
                             }else{
                                 this@SettingsView.sfxButton?.isChecked = true
                                 gamePreferences.storeMuteSound(true)
                             }
                             true
                         }
                     }
                 }
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
    gamePreferences: GamePreferences,
    init: SettingsView.(S) -> Unit = {}
): SettingsView = actor(SettingsView(skin,stageNavigator,gamePreferences),init)
