package com.libgdx.treasurehunter.enums

import com.badlogic.gdx.math.Vector2
import com.libgdx.treasurehunter.utils.GameObject

enum class MarkType(val offset : Vector2) {
    DEAD_MARK(Vector2(0f, 0.3f)),
    INTERROGATION_MARK(Vector2(0f, 0.3f)),
    EXCLAMATION_MARK(Vector2(0f, 0.3f));

    fun toGameObject() : GameObject{
        return when(this){
            DEAD_MARK -> GameObject.DEAD_MARK
            INTERROGATION_MARK -> GameObject.INTERROGATION_MARK
            EXCLAMATION_MARK -> GameObject.EXCLAMATION_MARK
        }
    }
}
