package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.EntityTags
import com.github.quillraven.fleks.entityTagOf

enum class EntityTag() : EntityTags by entityTagOf() {
    PLAYER,ALLIE,ENEMY,BACKGROUND,FOREGROUND,HAS_MARK,REMOVE,ATTACH_TO_SHIP,COLLECTED,RESPAWNABLE
}
