package com.libgdx.treasurehunter.ecs.components

import com.github.quillraven.fleks.EntityTags
import com.github.quillraven.fleks.entityTagOf
import com.libgdx.treasurehunter.enums.ParticleType

enum class EntityTag() : EntityTags by entityTagOf() {
    PLAYER,BACKGROUND,FOREGROUND,HAS_MARK,COLLECTABLE
}
