package com.libgdx.treasurehunter.ecs.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Blink
import com.libgdx.treasurehunter.ecs.components.Graphic

class BlinkSystem : IteratingSystem(family = family { all(Blink,Graphic) }) {

    override fun onTickEntity(entity: Entity) {
        val blinkComp = entity[Blink]
        val (sprite,_) = entity[Graphic]
        val(maxTime,blinksRatio,_) = blinkComp
        if (maxTime <= 0f){
            println("hey")
            sprite.setAlpha(1f)
            entity.configure { it -= Blink }
            return
        }
        println("maxTime: $maxTime")
        println("blinksRatio: $blinksRatio")
        println("timer: ${blinkComp.timer}")
        blinkComp.maxTime -= deltaTime
        blinkComp.timer += deltaTime
        if (blinkComp.timer >= blinksRatio){
            blinkComp.timer = 0f
            sprite.setAlpha(if (sprite.color.a == 0f) 1f else 0f)
        }

    }
}
