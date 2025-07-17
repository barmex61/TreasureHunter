package com.libgdx.treasurehunter.ui.view

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import ktx.scene2d.KTable
import ktx.scene2d.KVerticalGroup
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.horizontalGroup
import ktx.scene2d.image
import ktx.scene2d.label
import ktx.scene2d.stack
import ktx.scene2d.verticalGroup


class ToolTipView(
    skin: Skin = Scene2DSkin.defaultSkin
) : Table(skin), KTable {

    val verticalGroup : KVerticalGroup
    var updateOnce : Boolean = false
    init {
        this.touchable = Touchable.disabled
        stack {
            it.minWidth(125f)
            image("prefabs_9"){
                setScaling(Scaling.stretch)
            }
            this@ToolTipView.verticalGroup = verticalGroup {
                align(Align.top)
                fill()
                expand()
                padLeft(7.0f)
                padRight(7.0f)
                padTop(5.0f)
                padBottom(8.0f)
                space(4.0f)
            }
        }
    }

    fun KVerticalGroup.updateTooltip(title : String, properties : List<Pair<String, String>>, description : String){
        this@ToolTipView.verticalGroup.clearChildren()
        with(this){
            label(title){
                setAlignment(Align.center)
                color = this@ToolTipView.skin.getColor("red")
            }
            properties.forEach { (key, value) ->
                horizontalGroup {
                    label("$key : ","small"){
                        color = this@ToolTipView.skin.getColor("orange")
                    }
                    label(value,"small"){
                        color = this@ToolTipView.skin.getColor("green_dark")
                    }
                }
            }

            label(description,"very_small"){
                setAlignment(Align.center)
                setWrap(true)
                color = this@ToolTipView.skin.getColor("white_1")
            }
        }
    }
}

