package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.libgdx.treasurehunter.utils.GameObject
import ktx.math.vec2

data class Graphic(val sprite : Sprite,val gameObject: GameObject) : Component <Graphic> , Comparable<Graphic> {

    val center  = vec2()
        get(){
            field.x = sprite.x + sprite.width/2f
            field.y = sprite.y + sprite.height/2f
            return field
        }

    operator fun component3() = center

    override fun type() = Graphic

    companion object : ComponentType<Graphic>()

    override fun compareTo(other: Graphic): Int = when{
        sprite.y < other.sprite.y -> -1
        sprite.y > other.sprite.y -> 1
        sprite.x < other.sprite.x -> -1
        sprite.x > other.sprite.x -> 1
        else -> 0
    }

}
