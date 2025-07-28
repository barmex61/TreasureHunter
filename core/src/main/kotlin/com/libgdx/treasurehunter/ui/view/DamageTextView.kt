package com.libgdx.treasurehunter.ui.view

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.libgdx.treasurehunter.ui.model.DamageText
import ktx.math.random
import ktx.scene2d.KTable
import ktx.scene2d.label

class DamageTextView(damageText: DamageText, onDamageTextRemoved: (DamageTextView) -> Unit) : Table(), KTable {
    init {
        setFillParent(false)
        setPosition(damageText.position.x, damageText.position.y)
        val text = if (damageText.isCrit) "${damageText.damageAmount} Crit !" else "${damageText.damageAmount}"

        label(text = text) {
            this.color = Color.RED

            addAction(Actions.sequence(
                Actions.moveBy((-100f..100f).random(), (-100f..100f).random(), 1f),
                Actions.fadeOut(0.5f),
                Actions.run {
                    onDamageTextRemoved(this@DamageTextView)
                }
            ))
        }
    }
}
