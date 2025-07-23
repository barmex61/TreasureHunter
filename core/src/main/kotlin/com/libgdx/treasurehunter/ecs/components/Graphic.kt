package com.libgdx.treasurehunter.ecs.components

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.utils.GameObject
import ktx.math.vec2

data class Graphic(val sprite : Sprite,val modelName : String,val initialFlipX : Boolean = false) : Component <Graphic> , Comparable<Graphic> {

    val center  = vec2()
        get(){
            field.x = sprite.x + sprite.width/2f
            field.y = sprite.y + sprite.height/2f
            return field
        }

    val rotatedCenter : Vector2
        get() {
            val radians = MathUtils.degreesToRadians * sprite.rotation
            val localCenterX = sprite.width / 2f
            val localCenterY = sprite.height / 2f

            val rotatedCenterX = localCenterX * MathUtils.cos(radians) - localCenterY * MathUtils.sin(radians)
            val rotatedCenterY = localCenterX * MathUtils.sin(radians) + localCenterY * MathUtils.cos(radians)

            return vec2(sprite.x + rotatedCenterX, sprite.y + rotatedCenterY)
        }

    val upperRight = vec2()
        get()  {
            field.x = sprite.x + sprite.width
            field.y = sprite.y + sprite.height
            return field
        }
    val upperLeft = vec2()
        get()  {
            field.x = sprite.x
            field.y = sprite.y + sprite.height
            return field
        }
    val bottomLeft = vec2()
        get() {
            field.x = sprite.x
            field.y = sprite.y
            return field
        }
    val bottomRight = vec2()
        get() {
            field.x = sprite.x + sprite.width
            field.y = sprite.y
            return field
        }

    val bottomCenter = vec2()
        get() {
            field.x = sprite.x + sprite.width/2f
            field.y = sprite.y
            return field
        }

    var offset = vec2()

    override fun World.onRemove(entity: Entity) {
        sprite.texture = null
    }

    operator fun component4() = center

    override fun type() = Graphic

    companion object : ComponentType<Graphic>()

    override fun compareTo(other: Graphic): Int = when{
        modelName == GameObject.WATER.atlasKey && other.modelName != GameObject.WATER.atlasKey -> 1
        sprite.y < other.sprite.y -> -1
        sprite.y > other.sprite.y -> 1
        sprite.x < other.sprite.x -> -1
        sprite.x > other.sprite.x -> 1
        else -> 0
    }

}
