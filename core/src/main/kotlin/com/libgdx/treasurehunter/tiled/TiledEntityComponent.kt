package com.libgdx.treasurehunter.tiled

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityCreateContext
import com.github.quillraven.fleks.World
import com.libgdx.treasurehunter.ecs.components.AiComponent
import com.libgdx.treasurehunter.state.StateEntity
import com.libgdx.treasurehunter.state.PlayerState
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
import com.libgdx.treasurehunter.state.EntityState
import com.libgdx.treasurehunter.state.SwordState
import com.libgdx.treasurehunter.ecs.components.AnimationData
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.BlueDiamond
import com.libgdx.treasurehunter.ecs.components.BluePotion
import com.libgdx.treasurehunter.ecs.components.Breakable
import com.libgdx.treasurehunter.ecs.components.Chest
import com.libgdx.treasurehunter.ecs.components.Damage
import com.libgdx.treasurehunter.ecs.components.GoldCoin
import com.libgdx.treasurehunter.ecs.components.GoldenSkull
import com.libgdx.treasurehunter.ecs.components.GreenBottle
import com.libgdx.treasurehunter.ecs.components.GreenDiamond
import com.libgdx.treasurehunter.ecs.components.Inventory
import com.libgdx.treasurehunter.ecs.components.Item
import com.libgdx.treasurehunter.ecs.components.ItemData
import com.libgdx.treasurehunter.ecs.components.Key
import com.libgdx.treasurehunter.ecs.components.Life
import com.libgdx.treasurehunter.ecs.components.Map
import com.libgdx.treasurehunter.ecs.components.MapType
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.components.RedDiamond
import com.libgdx.treasurehunter.ecs.components.RedPotion
import com.libgdx.treasurehunter.ecs.components.Ship
import com.libgdx.treasurehunter.ecs.components.SilverCoin
import com.libgdx.treasurehunter.ecs.components.Stat
import com.libgdx.treasurehunter.ecs.components.Sword
import com.libgdx.treasurehunter.factory.AttackMetaDataFactory
import com.libgdx.treasurehunter.state.ChestState
import com.libgdx.treasurehunter.state.ShipState
import ktx.app.gdxError
import ktx.math.vec2
import ktx.tiled.property
import ktx.tiled.propertyOrNull


fun sprite(modelName: String, animationType: AnimationType, startPosition : Vector2, assetHelper: AssetHelper, rotation : Float = 0f,scale : Float = 1f,frameIx : Int = 0): Sprite {
    val regionPath = "${modelName}/${animationType.atlasKey}"
    val atlas = assetHelper[TextureAtlasAssets.GAMEOBJECT]
    val regions = atlas.findRegions(regionPath) ?:
    gdxError("There are no regions for $modelName and $animationType")
    val firstFrame = regions[frameIx]
    val w = firstFrame.regionWidth * UNIT_SCALE
    val h = firstFrame.regionHeight * UNIT_SCALE
    return Sprite(firstFrame).apply {
        setPosition(startPosition.x,startPosition.y)
        setSize(w,h)
        setOrigin(0f,0f)
        setScale(scale,scale)
        this.rotation = -rotation
    }
}

fun EntityCreateContext.configureEntityGraphic(entity: Entity,mapObject: TiledMapTileMapObject,position : Vector2,gameObject: GameObject,assetHelper: AssetHelper,world: World,rotation : Float ){
    val tile = mapObject.tile
    val startAnimType = AnimationType.valueOf(mapObject.propertyOrNull("startAnimType")?:tile.property("startAnimType","NONE"))
    val hasAnimation = mapObject.propertyOrNull<Boolean>("hasAnimation")?:tile.property("hasAnimation",false)
    val initialFlipX = tile.property<Boolean>("isFlipped",false)

    entity += Graphic(sprite(gameObject.atlasKey,startAnimType,position,assetHelper,rotation),gameObject.atlasKey,initialFlipX)
    if (startAnimType != AnimationType.NONE && hasAnimation){
        configureAnimation(entity,tile,startAnimType,gameObject)
    }
}

fun EntityCreateContext.configureAnimation(entity: Entity, tile: TiledMapTile,startAnimType : AnimationType,gameObject: GameObject) {
    val frameDuration = tile.property<Float>("animFrameDuration",0f)
    if (frameDuration == 0f) return
    entity += Animation(modelName = gameObject.atlasKey, animationData = AnimationData(
        frameDuration = frameDuration,
        animationType = startAnimType
    ))
}

fun EntityCreateContext.configureMove(entity: Entity, tile: TiledMapTile){
    val speed = tile.property<Float>("speed",0f)
    if (speed > 0f ){
        val timeToMax = tile.property<Float>("timeToMax",0.1f)
        entity += Move(timeToMax = timeToMax, maxSpeed = speed)
    }
}

fun EntityCreateContext.configureJump(entity: Entity, tile: TiledMapTile,gameObject: GameObject){
    val jumpHeight = tile.property<Float>("jumpHeight",0f)

    if (jumpHeight > 0f){
        val (body) = entity[Physic]
        val feetFixture = body.fixtureList.first { it.userData == "footFixture" }
        val polygonShape = feetFixture.shape as PolygonShape
        val lowerXY = vec2(Float.MAX_VALUE, Float.MAX_VALUE)
        val upperXY = vec2(Float.MIN_VALUE, Float.MIN_VALUE)
        val vertex = vec2()
        for (i in 0 until polygonShape.vertexCount) {
            polygonShape.getVertex(i, vertex)
            if ((vertex.y < lowerXY.y) || (vertex.y == lowerXY.y && vertex.x < lowerXY.x)) {
                lowerXY.y = vertex.y
                lowerXY.x = vertex.x
            }
            if ((vertex.y > upperXY.y) || (vertex.y == upperXY.y && vertex.x > upperXY.x)) {
                upperXY.y = vertex.y
                upperXY.x = vertex.x
            }
        }

        entity += Jump(jumpHeight,lowerXY ,upperXY, jumpSoundAsset = gameObject.toJumpSoundAsset() )
    }
}

@Suppress("UNCHECKED_CAST")
fun EntityCreateContext.configureState(entity: Entity, tile: TiledMapTile, world: World, physicWorld: PhysicWorld, assetHelper: AssetHelper) {
    val gameObjectStr = tile.property<String>("gameObject", "")
    when (gameObjectStr) {
        GameObject.CAPTAIN_CLOWN.name -> {
            val playerEntity = StateEntity.PlayerEntity(entity, world, physicWorld, assetHelper)
            entity += State(playerEntity, PlayerState.IDLE as EntityState<StateEntity>)
        }
        GameObject.CAPTAIN_CLOWN_SWORD.name -> {
            val playerEntity = StateEntity.PlayerEntity(entity, world, physicWorld, assetHelper)
            entity += State(playerEntity, PlayerState.IDLE as EntityState<StateEntity>)
        }
        GameObject.SWORD.name -> {
            val swordEntity = StateEntity.SwordEntity(entity, world, physicWorld, assetHelper)
            entity += State(swordEntity, SwordState.IDLE as EntityState<StateEntity>)
        }
        GameObject.SHIP.name -> {
            val shipEntity = StateEntity.ShipEntity(entity, world, physicWorld, assetHelper)
            entity += State(shipEntity, ShipState.IDLE as EntityState<StateEntity>)
        }
        GameObject.CHEST.name ->{
            val chestEntity = StateEntity.ChestEntity(entity, world, physicWorld, assetHelper)
            entity += State(chestEntity, ChestState.IDLE as EntityState<StateEntity>)
        }
        GameObject.CHEST_LOCKED.name ->{
            val chestEntity = StateEntity.ChestEntity(entity, world, physicWorld, assetHelper)
            entity += State(chestEntity, ChestState.IDLE as EntityState<StateEntity>)
        }

        else -> return
    }
}

fun EntityCreateContext.configureChest(entity: Entity, tile: TiledMapTile,mapObject: TiledMapTileMapObject) {
    val gameObjectStr = tile.property<String>("gameObject", "")
    entity += when(gameObjectStr){
        GameObject.CHEST.name -> {
            Chest(GameObject.valueOf(gameObjectStr))
        }
        GameObject.CHEST_LOCKED.name -> {
            Chest(GameObject.valueOf(gameObjectStr), isLocked = true)
        }
        else -> return
    }
    val itemsCsv = mapObject.propertyOrNull<String>("itemsInside") ?: return
    val items: List<ItemEntry> = itemsCsv.split(',').mapNotNull { entry ->
        val parts = entry.split(':')
        if (parts.size != 2) null else ItemEntry(parts[0],parts[1].toIntOrNull()?:1)
    }
    entity[Chest].itemsInside = items
}

fun EntityCreateContext.configureBreakable(entity: Entity, tile: TiledMapTile,mapObject: TiledMapTileMapObject) {
    if (entity hasNo EntityTag.BREAKABLE) return
    val hitCount = mapObject.propertyOrNull<Int>("hitCount") ?: tile.property("hitCount", 2)
    val itemsCsv = mapObject.propertyOrNull<String>("itemsInside") ?: tile.property("itemsInside") ?: ""
    val itemsInside = if (itemsCsv.isNotBlank()) {
        itemsCsv.split(',').mapNotNull { entry ->
            val parts = entry.split(':')
            if (parts.size == 2) ItemEntry(parts[0], parts[1].toIntOrNull() ?: 1) else null
        }
    } else emptyList()

    entity += Breakable(hitCount = hitCount, itemsInside = itemsInside)

}


data class ItemEntry(
    val type : String,
    val count : Int
)

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
    val damage = tile.property<Int>("damage",0)
    if (damage > 0f){
        entity += Damage(damage = damage, sourceEntity = entity,false)
    }
}

fun EntityCreateContext.configureInventory(entity: Entity, gameObject: GameObject){
    if (gameObject == GameObject.CAPTAIN_CLOWN || gameObject == GameObject.CAPTAIN_CLOWN_SWORD) {
       entity += Inventory(owner = entity)
    }else return
}

fun EntityCreateContext.configureItem(entity: Entity, gameObject: GameObject){
    val itemType = when(gameObject){
        GameObject.SWORD -> Sword(1)
        GameObject.BIG_MAP -> Map(MapType.BIG_MAP)
        GameObject.SMALL_MAP_1 -> Map(MapType.SMALL_MAP_1)
        GameObject.SMALL_MAP_2 -> Map(MapType.SMALL_MAP_2)
        GameObject.SMALL_MAP_3 -> Map(MapType.SMALL_MAP_3)
        GameObject.SMALL_MAP_4 -> Map(MapType.SMALL_MAP_4)
        GameObject.KEY -> Key
        GameObject.BLUE_DIAMOND -> BlueDiamond
        GameObject.GREEN_DIAMOND -> GreenDiamond
        GameObject.RED_DIAMOND -> RedDiamond
        GameObject.GOLD_COIN -> GoldCoin
        GameObject.SILVER_COIN -> SilverCoin
        GameObject.GOLDEN_SKULL -> GoldenSkull
        GameObject.BLUE_POTION -> BluePotion
        GameObject.RED_POTION -> RedPotion(1)
        GameObject.GREEN_BOTTLE-> GreenBottle
        else -> return
    }
    entity += Item(itemData = ItemData(itemType))
}

fun EntityCreateContext.configureLife(entity: Entity, tile: TiledMapTile){
    val life = tile.property<Int>("life",0)
    if (life > 0f){
        entity += Life(maxLife = life, owner = entity)
    }
}

fun EntityCreateContext.configureStat(entity: Entity,tile: TiledMapTile){
    val stat = tile.propertyOrNull<Stat>("stat")
    if (stat != null){
        entity += Stat(
            attack = stat.attack,
            defense = stat.defense,
            critChance = stat.critChance,
            critDamage = stat.critDamage,
            resistance = stat.resistance
        )
    }
}

fun EntityCreateContext.configureAi(entity: Entity, tile: TiledMapTile,physicWorld: PhysicWorld){
    val aiTreePath = tile.property<String>("aiTreePath","")
    if (aiTreePath.isNotBlank()){
        val aiWanderRadius = tile.property<Float>("circleRadius")
        entity += AiComponent(treePath = aiTreePath, physicWorld = physicWorld, aiWanderRadius = aiWanderRadius)
    }
}

fun EntityCreateContext.configureAttack(entity: Entity, tile: TiledMapTile){
    val hasAttack = tile.property<Boolean>("hasAttack",false)
    val gameObject = GameObject.valueOf(tile.propertyOrNull<String>("gameObject")?: gdxError("gameObject is null $tile"))
    if (hasAttack){
        entity += Attack(
            attackMetaData = AttackMetaDataFactory.create(gameObject = gameObject)
        )
    }
}

fun EntityCreateContext.configureShip(entity: Entity, tile: TiledMapTile){
    val gameObjectStr = tile.property<String>("gameObject","")
    if (gameObjectStr == GameObject.SHIP.name){
        entity += Ship()
    }
}





