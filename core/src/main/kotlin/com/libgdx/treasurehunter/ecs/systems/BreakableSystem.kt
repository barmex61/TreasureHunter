package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.ai.pfa.Graph
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Blink
import com.libgdx.treasurehunter.ecs.components.Breakable
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Flash
import com.libgdx.treasurehunter.ecs.components.FlashType
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Invulnarable
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.components.Remove
import com.libgdx.treasurehunter.ecs.systems.DebugSystem.Companion.DEBUG_RECT
import com.libgdx.treasurehunter.ecs.systems.DebugSystem.Companion.JUMP_DEBUG_RECT
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.ShaderEffect
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.tiled.sprite
import com.libgdx.treasurehunter.utils.Constants.OBJECT_FIXTURES
import com.libgdx.treasurehunter.utils.GameObject
import com.libgdx.treasurehunter.utils.animation
import com.libgdx.treasurehunter.utils.copy
import com.libgdx.treasurehunter.utils.createBody
import com.libgdx.treasurehunter.utils.createFixtures
import com.libgdx.treasurehunter.utils.plus
import com.libgdx.treasurehunter.utils.spawnItems
import ktx.app.gdxError
import ktx.math.random
import ktx.math.vec2

class BreakableSystem : IteratingSystem(
    family = family { all(Breakable).none(Remove, Invulnarable) }
) {
    override fun onTickEntity(entity: Entity) {
        val breakable = entity[Breakable]
        var (itemsInside, hitCount, damageTaken) = breakable
        val graphic = entity[Graphic]
        hitCount = (hitCount - damageTaken).coerceAtLeast(0)

        if (damageTaken != 0) {
            entity.configure {
                it += Invulnarable(0.6f)
                it += Blink(maxTime = 0.6f, blinkRatio = 0.075f)
                it += Flash(ShaderEffect.RED_EFFECT, flashTimer = 0.5f, flashType = FlashType.BLINK)
                world.animation(
                    it,
                    AnimationType.HIT,
                    com.badlogic.gdx.graphics.g2d.Animation.PlayMode.NORMAL,
                    0.16f
                )
            }
        }

        if (hitCount == 0) {
            breakEntity(entity)
            if (itemsInside.isNotEmpty()) {
                world.spawnItems(itemsInside, entity[Graphic].rotatedCenter)
                breakable.itemsInside = listOf()
            }
            return
        }
        breakable.hitCount = hitCount
        breakable.damageTaken = 0
    }

    private fun breakEntity(entity: Entity) {
        val graphic = entity[Graphic]
        val assetHelper = world.inject<AssetHelper>()
        val physicWorld = world.inject<PhysicWorld>()
        (0..3).forEach { index ->
            val (position,impulse) = when (index) {
                0 -> Pair(graphic.rotatedCenter + vec2(-0.2f,0.2f),vec2((-3.5f..0f).random(),(0f..4f).random()))
                1 -> Pair(graphic.rotatedCenter + vec2(0.2f,0.2f),vec2((0f..3.5f).random(),(0f..4f).random()))
                2 -> Pair(graphic.rotatedCenter + vec2(-0.2f,-0.2f),vec2((-3.5f..0f).random(),(0f..3f).random()))
                3 -> Pair(graphic.rotatedCenter + vec2(0.2f,-0.2f),vec2((0f..3.5f).random(),(0f..3f).random()))
                else -> Pair(graphic.rotatedCenter,vec2((-1.5f..0f).random(),(0f..2f).random()))
            }
            world.entity {
                val body = physicWorld.createBody(
                    BodyDef.BodyType.DynamicBody,
                    position,
                    isFixedRotation = false
                )
                it += Graphic(
                    sprite(
                        graphic.modelName,
                        AnimationType.DESTROYED,
                        body.position,
                        assetHelper,
                        frameIx = index
                    ), graphic.modelName
                )
                it += Physic(body)
                it += Remove(
                    instantRemove = false,
                    removeTimer = 2f
                )
                body.applyLinearImpulse(impulse, body.worldCenter , true)
            }
        }
        entity.configure {
            it += Remove()
        }
    }

}
