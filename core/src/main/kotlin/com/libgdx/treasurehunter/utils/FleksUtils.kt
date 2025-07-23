package com.libgdx.treasurehunter.utils

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationData
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Chest
import com.libgdx.treasurehunter.ecs.components.Effect
import com.libgdx.treasurehunter.ecs.components.Flash
import com.libgdx.treasurehunter.ecs.components.FlashType
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Item
import com.libgdx.treasurehunter.ecs.components.ItemData
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.systems.AnimationSystem
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.EffectType
import com.libgdx.treasurehunter.enums.ShaderEffect
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.tiled.ItemEntry
import com.libgdx.treasurehunter.tiled.sprite
import com.libgdx.treasurehunter.utils.Constants.OBJECT_FIXTURES
import ktx.app.gdxError
import ktx.math.random
import ktx.math.vec2

fun World.animation(entity: Entity,animationType: AnimationType,playMode : PlayMode = PlayMode.LOOP,frameDuration : Float? = null) {
    val animationSystem = this.system<AnimationSystem>()
    animationSystem.setAnimation(entity,animationType,playMode,frameDuration)
}

fun World.createMarkEntity(owner : Entity,assetHelper : AssetHelper,effectType : EffectType,stickToOwner : Boolean = false){
    val spritePosition = if (stickToOwner) owner[Graphic].center else owner[Graphic].bottomLeft
    val gameObject = GameObject.EFFECT
    this.entity { markEntity ->
        markEntity += Graphic(
            sprite(
                gameObject.atlasKey,
                effectType.animationType,
                spritePosition,
                assetHelper,
                0f
            ),
            gameObject.atlasKey
        )
        markEntity += Animation(
            gameObject.atlasKey, AnimationData(
                animationType = effectType.animationType,
                playMode = PlayMode.NORMAL,
                frameDuration = 0.2f
            )
        )
        markEntity +=  Flash(
            shaderEffect = effectType.shaderEffect, flashType = FlashType.ONCE
        )
        markEntity += Effect(1f, effectType.offset, spritePosition,owner,stickToOwner)
    }
}

fun World.spawnItems(itemsToCreate: List<ItemEntry>,spawnPosition : Vector2) {
    println("spawnItems items ${itemsToCreate.joinToString()}")
    val physicWorld = this.inject<PhysicWorld>()
    val assetHelper = this.inject<AssetHelper>()
    itemsToCreate.forEach { itemEntry ->
        val gameObject = GameObject.valueOf(itemEntry.type)
        (1..itemEntry.count).forEach { index ->
            this.entity { entity ->
                val fixtureUserData = OBJECT_FIXTURES[gameObject] ?: gdxError("No fixture definition for $gameObject")
                val nonSensorFixtures = fixtureUserData.map { it.copy(fixtureDef = it.fixtureDef.copy(isSensor = false)) }
                val body = physicWorld.createBody(
                    BodyDef.BodyType.DynamicBody, spawnPosition,
                ).also { body ->
                    body.createFixtures(nonSensorFixtures)
                    body.userData = entity
                }
                entity += Physic(body)
                val itemType = gameObject.toItemType()
                entity += Item(ItemData(gameObject.toItemType()))
                val shaderAndFlashType = itemType.getShaderEffectAndFlashType()
                val shaderEffect = shaderAndFlashType.first
                val flashType = shaderAndFlashType.second
                entity += Flash(
                    shaderEffect,
                    flashTimer = 1f,
                    flashDuration = 0.1f,
                    flashInterval = 0.25f,
                    flashType = flashType
                )
                entity += Graphic(
                    sprite(
                        gameObject.atlasKey,
                        AnimationType.IDLE,
                        body.position,
                        assetHelper,
                        0f
                    ),
                    gameObject.atlasKey
                )
                entity += Animation(
                    gameObject.atlasKey,
                    AnimationData(
                        animationType = AnimationType.IDLE,
                        playMode = PlayMode.LOOP
                    )
                )
                val impulse =
                    vec2(body.mass * (-3f..3f).random(), body.mass * (0f..5f).random())
                body.applyLinearImpulse(impulse, body.worldCenter, true)
            }
        }
    }

}

