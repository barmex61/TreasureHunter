package com.libgdx.treasurehunter.ui.view

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher
import com.libgdx.treasurehunter.ui.model.ButtonType
import com.libgdx.treasurehunter.ui.model.DialogModel
import com.libgdx.treasurehunter.ui.model.DialogTarget
import ktx.actors.onClick
import ktx.scene2d.KTable
import ktx.scene2d.container
import ktx.scene2d.horizontalGroup
import ktx.scene2d.imageTextButton
import ktx.scene2d.label
import ktx.scene2d.verticalGroup

class DialogView(
    skin: Skin,
    dialogModel: DialogModel = DialogModel().also {
        GameEventDispatcher.registerListener(it)
    },
    toggleDialogView: DialogView.() -> Unit,
) : Table(skin), KTable {

    private val textLabel: Label
    private var dialogTarget: DialogTarget? = null
    private val horizontalButtonGroup: HorizontalGroup

    init {
        dialogModel.onDialogTargetChange = { target ->
            dialogTarget = target
            textLabel.setText(target.dialogText)
            handleHorizontalButtonGroup(target,toggleDialogView)
            toggleDialogView()
        }

        setFillParent(true)
        container {
            this.background = skin.getDrawable("prefabs_9")
            fill()
            padLeft(10.0f)
            padRight(10.0f)
            padTop(10.0f)
            padBottom(10.0f)
            verticalGroup {
                expand()
                fill()
                space(10.0f)
                this@DialogView.textLabel = label("") {
                    setAlignment(Align.center)
                    wrap = true
                }
                this@DialogView.horizontalButtonGroup = horizontalGroup {
                    align(Align.center)
                    space(15f)
                    imageTextButton("Yes", "yes") {
                        onClick {
                            this@DialogView.dialogTarget?.let { target ->
                                GameEventDispatcher.fireEvent(
                                    GameEvent.OpenChestEvent(
                                        target.entity
                                    )
                                )
                            }
                            this@DialogView.toggleDialogView()
                        }
                    }
                    imageTextButton("No", "no") {
                        onClick {
                            this@DialogView.toggleDialogView()
                        }
                    }
                }
            }
        }
    }

    private fun DialogView.handleHorizontalButtonGroup(target: DialogTarget,toggleDialogView: DialogView.() -> Unit) {
        horizontalButtonGroup.clearChildren()
        target.buttonTypes.forEach { buttonType ->
            when (buttonType) {
                ButtonType.YES -> horizontalButtonGroup.addActor(
                    imageTextButton("Yes", "yes") {
                        onClick {
                            GameEventDispatcher.fireEvent(
                                GameEvent.OpenChestEvent(target.entity)
                            )
                            this@DialogView.toggleDialogView()
                        }
                    }
                )

                ButtonType.NO -> horizontalButtonGroup.addActor(
                    imageTextButton("No", "no") {
                        onClick {
                            this@DialogView.toggleDialogView()
                        }
                    }
                )

                ButtonType.OK -> horizontalButtonGroup.addActor(
                    imageTextButton("OK", "yes") {
                        onClick {
                            this@DialogView.toggleDialogView()
                        }
                    }
                )
            }
        }
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        val oldColor = batch!!.color.cpy()
        super.draw(batch, parentAlpha)
        batch.color = oldColor
    }
}
