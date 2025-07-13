package com.libgdx.treasurehunter.ui.model

import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventListener

enum class DialogTargetType{
    CHEST,
    NPC,
    QUEST
}

data class DialogTarget(
    val type: DialogTargetType,
    val entity : Entity,
    val dialogText: String
)

enum class DialogResult{
    YES,NO
}
class DialogModel(
    var dialogTarget: DialogTarget? = null,
    var onDialogTargetChange : (DialogTarget) -> Unit = {},
) : GameEventListener {

    override fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.ChestClickedEvent -> {
                dialogTarget = DialogTarget(
                    type = DialogTargetType.CHEST,
                    entity = event.chestEntity,
                    dialogText = event.dialogText
                )
                onDialogTargetChange(dialogTarget!!)
            }
            else -> Unit
        }
    }

}
