package com.libgdx.treasurehunter.ui.view

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.libgdx.treasurehunter.ui.model.GameModel
import ktx.scene2d.KTable
import ktx.scene2d.KWidget
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actor
import ktx.scene2d.container
import ktx.scene2d.horizontalGroup
import ktx.scene2d.image
import ktx.scene2d.verticalGroup
import ktx.scene2d.stack


class InventoryView(
    skin: Skin,
    gameModel: GameModel,
) : Table(skin), KTable {

    init {

        setFillParent(true)

        verticalGroup {

            fill()
            container {
                prefWidth(200.0f)
                prefHeight(45.0f)
                stack {
                    image("prefabs_4_ninepatch")
                    horizontalGroup {
                        align(Align.center)
                        image("big_text_i")
                        image("big_text_n")
                        image("big_text_v")
                        image("big_text_e")
                        image("big_text_n")
                        image("big_text_t")
                        image("big_text_o")
                        image("big_text_r")
                        image("big_text_y")
                    }
                }
            }
            stack {
                image("prefabs_6")
                verticalGroup {
                    pad(20f)
                    repeat(5){
                        horizontalGroup {
                            repeat(5) {
                                container {
                                    prefWidth(36.0f)
                                    prefHeight(32.0f)
                                    padLeft(2.0f)
                                    background(skin.getDrawable("text_bubble"))
                                    stack {
                                        container {
                                            align(Align.bottomRight)
                                            prefSize(8.0f)
                                            padRight(2.0f)
                                            padBottom(2.0f)
                                            image("big_text_6")
                                        }
                                        image("sword_on") {
                                            setScaling(Scaling.none)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        verticalGroup {
            container {
                prefWidth(145.0f)
                prefHeight(45.0f)
                stack {
                    image("prefabs_4_ninepatch")
                    horizontalGroup {
                        align(Align.center)
                        image("big_text_g")
                        image("big_text_e")
                        image("big_text_a")
                        image("big_text_r")
                    }
                }
            }
            stack {
                image("prefabs_5")
                verticalGroup {
                    padTop(17f)
                    padBottom(17f)
                    space(-4f)
                    container {
                        background(skin.getDrawable("text_bubble"))
                        prefWidth(36f)
                        prefHeight(32f)
                        image("helmet_off"){
                            setScaling(Scaling.none)
                        }
                    }
                    horizontalGroup {
                        space(-4f)
                        container {
                            background(skin.getDrawable("text_bubble"))
                            prefHeight(32f)
                            prefWidth(36f)
                            image("sword_off"){
                                setScaling(Scaling.none)
                            }
                        }
                        container {
                            background(skin.getDrawable("text_bubble"))
                            prefHeight(32f)
                            prefWidth(36f)
                            image("armor_off"){
                                setScaling(Scaling.none)
                            }
                        }
                    }
                    container {
                        background(skin.getDrawable("text_bubble"))
                        prefWidth(36f)
                        prefHeight(32f)
                        image("boots_off"){
                            setScaling(Scaling.none)
                        }
                    }
                }
            }
        }

    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val oldColor = batch!!.color.cpy()
        super.draw(batch, parentAlpha)
        batch.color = oldColor
    }
}

