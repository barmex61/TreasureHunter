package com.libgdx.treasurehunter.tiled

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.ChainShape
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityCreateContext
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ai.AiEntity
import com.libgdx.treasurehunter.ai.PlayerState
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.AnimationType
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Jump
import com.libgdx.treasurehunter.ecs.components.Move
import  com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.enums.TextureAtlasAssets
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.utils.Constants.UNIT_SCALE
import com.libgdx.treasurehunter.utils.GameObject
import com.libgdx.treasurehunter.utils.animation
import com.libgdx.treasurehunter.ai.EntityState
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.Damage
import com.libgdx.treasurehunter.ecs.components.Life
import com.libgdx.treasurehunter.ecs.components.Physic
import ktx.app.gdxError
import ktx.math.vec2
import ktx.tiled.property
import ktx.tiled.propertyOrNull


fun sprite(gameObject: GameObject, animationType: AnimationType, startPosition : Vector2,assetHelper: AssetHelper, rotation : Float = 0f): Sprite {
    val regionPath = "${gameObject.atlasKey}/${animationType.atlasKey}"
    val atlas = assetHelper[TextureAtlasAssets.GAMEOBJECT]
    val regions = atlas.findRegions(regionPath) ?:
    gdxError("There are no regions for $gameObject and $animationType")
    val firstFrame = regions.first()
    val w = firstFrame.regionWidth * UNIT_SCALE
    val h = firstFrame.regionHeight * UNIT_SCALE
    return Sprite(firstFrame).apply {
        setPosition(startPosition.x,startPosition.y)
        setSize(w,h)
        setOrigin(0f,0f)
        this.rotation = -rotation
    }
}

fun EntityCreateContext.configureEntityGraphic(entity: Entity,tile: TiledMapTile,position : Vector2,gameObject: GameObject,assetHelper: AssetHelper,world: World,rotation : Float ){
    val startAnimType = AnimationType.valueOf(tile.property("startAnimType","NONE"))
    entity += Graphic(sprite(gameObject,startAnimType,position,assetHelper,rotation),gameObject)
    if (startAnimType != AnimationType.NONE){
        configureAnimation(entity,tile,world,startAnimType,gameObject)
    }
}

fun EntityCreateContext.configureAnimation(entity: Entity, tile: TiledMapTile, world: World,startAnimType : AnimationType,gameObject: GameObject) {
    if (tile.property<Float>("animFrameDuration",0f) == 0f) return
    entity += Animation(frameDuration = tile.property<Float>("animFrameDuration"), gameObject = gameObject)
    world.animation(entity,startAnimType,)
}

fun EntityCreateContext.configureMove(entity: Entity, tile: TiledMapTile){
    val speed = tile.property<Float>("speed",0f)
    if (speed > 0f ){
        val timeToMax = tile.property<Float>("timeToMax",0.1f)
        entity += Move(timeToMax = timeToMax, maxSpeed = speed)
    }
}

fun EntityCreateContext.configureJump(entity: Entity, tile: TiledMapTile){
    val jumpHeight = tile.property<Float>("jumpHeight",0f)

    if (jumpHeight > 0f){
        val (body) = entity[Physic]
        val feetFixture = body.fixtureList.first { it.userData == "footFixture" }
        val chainShape = feetFixture.shape as ChainShape
        val lowerXY = vec2(100f,100f)
        val upperXY = vec2(-100f,-100f)
        val vertex = vec2()
        for (i in 0 until chainShape.vertexCount){
            chainShape.getVertex(i,vertex)
            if (vertex.y <= lowerXY.y && vertex.x <= lowerXY.x){
                lowerXY.set(vertex)
            }else if (vertex.y >= upperXY.y && vertex.x >= upperXY.x){
                upperXY.set(vertex)
            }
        }
        if ((lowerXY.x == 100f && lowerXY.y == 100f) || (upperXY.x == 100f && upperXY.y == 100f)){
            gdxError("Couldnt calculate feet fixture size of entity $entity and tile $tile")
        }
        entity += Jump(jumpHeight,lowerXY ,upperXY )
    }
}

fun EntityCreateContext.configureState(entity: Entity, tile: TiledMapTile,world: World,physicWorld : PhysicWorld){
    val gameObjectStr = tile.property<String>("gameObject","")
    val state : EntityState = when(gameObjectStr){
        GameObject.CAPTAIN_CLOWN.name -> PlayerState.IDLE
        GameObject.CAPTAIN_CLOWN_SWORD.name -> PlayerState.IDLE

        else -> return
    }
    entity += State(AiEntity(entity, world, physicWorld),state)
}

fun EntityCreateContext.configureEntityTags(
    entity: Entity,
    mapObject: TiledMapTileMapObject,
    tile: TiledMapTile,
){
    val entityTags = mapObject.propertyOrNull<String>("entityTags")?:tile.property("entityTags","")
    if (entityTags.isNotBlank()){
        val tags = entityTags.split(",").map { splitEntityTag -> EntityTag.valueOf(splitEntityTag)}
        entity += tags
    }
}

fun EntityCreateContext.configureDamage(entity: Entity, tile: TiledMapTile){
    val damage = tile.property<Int>("bodyDamage",0)
    if (damage > 0f){
        entity += Damage(damage = damage)
    }
}

fun EntityCreateContext.configureAttack(entity: Entity, tile: TiledMapTile){
    val damage = tile.property<Float>("attackDamage",0f)
    if (damage > 0f){
        entity += Attack(attackDamage= damage)
    }
}

fun EntityCreateContext.configureLife(entity: Entity, tile: TiledMapTile){
    val life = tile.property<Int>("life",0)
    if (life > 0f){
        entity += Life(maxLife = life)
    }
}

