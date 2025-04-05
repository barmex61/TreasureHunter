package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.EntityTags
import com.github.quillraven.fleks.entityTagOf

enum class EntityTag() : EntityTags by entityTagOf() {
    PLAYER
}
