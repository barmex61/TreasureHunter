package com.libgdx.treasurehunter.ui.utils

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Stack
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.libgdx.treasurehunter.ui.model.GameModel
import com.libgdx.treasurehunter.ui.view.GameView
import ktx.scene2d.KGroup
import ktx.scene2d.KStack
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.horizontalGroup
import ktx.scene2d.image
import ktx.scene2d.stack
import ktx.scene2d.verticalGroup

class TextStack(
    val backgroundPath : String = "prefabs_12",
    val textAlignment : Int = Align.center,
    val stackWidth : Float = -1f,
    val stackHeight : Float = -1f,
    val spacingBetweenChars : Float = 1f,
    val padBackground : Float = 10f,
): Stack(),KGroup{
    var text = "HELLO WORLD"
   init {
       val length = text.length
       val lineCount = length / 30
       stack{

           image(drawableName = this@TextStack.backgroundPath)
           verticalGroup{
               align(this@TextStack.textAlignment)
               (0..lineCount).forEach { lineIndex ->
                   horizontalGroup{
                       pad(this@TextStack.padBackground)
                       space(this@TextStack.spacingBetweenChars)
                       (0..30).forEach { charIndex ->
                           if (lineIndex * 30 + charIndex >= length) return@forEach
                           var char = this@TextStack.text[lineIndex * 30 + charIndex]
                           image(drawableName = "big_text_${if (char.isWhitespace()) "space" else char.lowercase()}")
                       }
                   }
               }
           }
       }
   }
}
@Scene2dDsl
fun <S> KWidget<S>.textStack(
    backgroundPath : String = "prefabs_12",
    textAlignment : Int = Align.center,
    stackWidth : Float = -1f,
    stackHeight : Float = -1f,
    spacingBetweenChars : Float = 1f,
    padBackground : Float = 10f,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: TextStack.(S) -> Unit = {}
): TextStack = actor(TextStack(
    backgroundPath = backgroundPath,
    textAlignment = textAlignment,
    stackWidth = stackWidth,
    stackHeight = stackHeight,
    spacingBetweenChars = spacingBetweenChars,
    padBackground = padBackground
),init)
