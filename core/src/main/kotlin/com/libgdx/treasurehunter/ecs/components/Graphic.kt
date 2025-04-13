package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.utils.GameObject
import ktx.math.vec2

data class Graphic(val sprite : Sprite) : Component <Graphic> , Comparable<Graphic> {

    val center  = vec2()
        get(){
            field.x = sprite.x + sprite.width/2f
            field.y = sprite.y + sprite.height/2f
            return field
        }
    val upperLeft = vec2()
        get()  {
            field.x = sprite.x
            field.y = sprite.y + sprite.height
            return field
        }
    val bottomCenter = vec2()
        get() {
            field.x = sprite.x + sprite.width/2f
            field.y = sprite.y
            return field
        }

    var effectOffset = vec2()

    override fun World.onRemove(entity: Entity) {
        sprite.texture = null
    }

    operator fun component2() = center

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
