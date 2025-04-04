package com.libgdx.treasurehunter.tiled

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
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
import ktx.app.gdxError
import ktx.tiled.property
import ktx.tiled.propertyOrNull


fun sprite(gameObject: GameObject, animationType: AnimationType, startPosition : Vector2,assetHelper: AssetHelper, rotation : Float = 0f): Sprite {
    val animationPath = "${gameObject.atlasKey}/${animationType.atlasKey}"
    val atlas = assetHelper[TextureAtlasAssets.GAMEOBJECT]
    val regions = atlas.findRegions(animationPath) ?:
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

fun EntityCreateContext.configureEntityGraphic(entity: Entity,tile: TiledMapTile,body: Body,gameObject: GameObject,assetHelper: AssetHelper,world: World,rotation : Float ){
    val startAnimType = AnimationType.valueOf(tile.property("startAnimType","NONE"))
    if (startAnimType != AnimationType.NONE){
        entity += Graphic(sprite(gameObject,startAnimType,body.position,assetHelper,rotation))
        configureAnimation(entity,tile,world,startAnimType,gameObject)
    }
}

fun EntityCreateContext.configureAnimation(entity: Entity, tile: TiledMapTile, world: World,startAnimType : AnimationType,gameObject: GameObject) {
    if (tile.property<Float>("animFrameDuration") == 0f) return
    entity += Animation(frameDuration = tile.property<Float>("animFrameDuration"), gameObject = gameObject)
    world.animation(entity,startAnimType)
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
        entity += Jump(jumpHeight)
    }
}

fun EntityCreateContext.configureState(entity: Entity, tile: TiledMapTile,world: World,physicWorld : PhysicWorld){
    val gameObjectStr = tile.property<String>("gameObject","")
    val state : EntityState = when(gameObjectStr){
        GameObject.CAPTAIN_CLOWN.name -> PlayerState.IDLE
        GameObject.CAPTAIN_CLOWN_SWORD.name -> PlayerState.IDLE
        "" -> return
        else -> gdxError("gameObject $gameObjectStr doesn't support ")
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

