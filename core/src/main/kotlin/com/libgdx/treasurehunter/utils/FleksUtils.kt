package com.libgdx.treasurehunter.utils

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.systems.AnimationSystem

fun World.animation(entity: Entity,animationType: AnimationType,playMode : PlayMode = PlayMode.LOOP) {
    val animationSystem = this.system<AnimationSystem>()
    animationSystem.entityAnimation(entity,animationType,playMode)
}
