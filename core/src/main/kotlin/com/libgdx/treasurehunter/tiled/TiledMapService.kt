package com.libgdx.treasurehunter.tiled

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody
import com.badlogic.gdx.physics.box2d.World
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventListener
import com.libgdx.treasurehunter.utils.Constants.UNIT_SCALE
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.math.MathUtils
import com.github.quillraven.fleks.Entity
import com.libgdx.treasurehunter.ecs.components.Animation
import com.libgdx.treasurehunter.ecs.components.Attack
import com.libgdx.treasurehunter.ecs.components.AttackMeta
import com.libgdx.treasurehunter.ecs.components.Damage
import com.libgdx.treasurehunter.ecs.components.EntityTag
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Jump
import com.libgdx.treasurehunter.ecs.components.Move
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.ecs.components.State
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.utils.Constants.OBJECT_FIXTURES
import com.libgdx.treasurehunter.utils.GameObject
import com.libgdx.treasurehunter.utils.createBody
import com.libgdx.treasurehunter.utils.createFixtures
import com.libgdx.treasurehunter.utils.fixtureDefinitionOf
import ktx.app.gdxError
import ktx.assets.disposeSafely
import ktx.math.vec2
import ktx.tiled.property
import ktx.tiled.x
import ktx.tiled.y


class TiledMapService(
    private val physicWorld: World,
    private val world: com.github.quillraven.fleks.World,
    private val assetHelper: AssetHelper,
) : GameEventListener {


    override fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.MapChangeEvent -> {
                spawnEntities(event.tiledMap)
            }

            else -> Unit
        }
    }

    private fun TiledMapTileLayer.forEachCell(onCell: (TiledMapTileLayer.Cell, Int, Int) -> Unit) {
        val width = this.width
        val height = this.height
        (0..width).forEach { x ->
            (0..height).forEach { y ->
                this.getCell(x, y)?.let { cell ->
                    onCell(cell, x, y)
                }
            }
        }
    }

    private fun spawnEntities(tiledMap: TiledMap) {
        // spawn static ground bodies
        tiledMap.layers.filterIsInstance<TiledMapTileLayer>().forEach { tileLayer ->
            tileLayer.forEachCell { cell, x, y ->
                cell.tile.objects.forEach { mapObject ->
                    spawnStaticObject(mapObject, x, y)
                }
            }
        }

        //spawn dynamic bodies
        with(tiledMap.layers.first { it.name == "objects" }) {
            this.objects.forEach { mapObject ->
                spawnEntity(mapObject)
            }
        }
    }

    private fun spawnEntity(mapObject: MapObject) {
        if (mapObject !is TiledMapTileMapObject) {
            gdxError("mapObject of type $mapObject is not supported")
        }
        val tile = mapObject.tile
        val gameObjectStr = tile.property<String>("gameObject")
        val gameObject = GameObject.valueOf(gameObjectStr)
        val fixtureDefUserData = OBJECT_FIXTURES[gameObject]

        if (fixtureDefUserData == null) {
            spawnBodilessEntities(mapObject, tile, gameObject)
            return
        }

        val bodyType = tile.property<String>("bodyType", "StaticBody")
        val gravityScale = tile.property<Float>("gravityScale", 1f)
        val rotation = mapObject.rotation
        val x = mapObject.x * UNIT_SCALE
        val y = mapObject.y * UNIT_SCALE
        val body = physicWorld.createBody(BodyType.valueOf(bodyType), vec2(x, y), rotation).apply {
            this.gravityScale = gravityScale
            this.setTransform(this.position, -rotation * MathUtils.degreesToRadians)
        }
        body.createFixtures(fixtureDefUserData)
        world.entity {
            body.userData = it
            it += Physic(body)
            configureEntityGraphic(
                it,
                mapObject,
                body.position,
                gameObject,
                assetHelper,
                world,
                rotation
            )
            configureEntityTags(it, mapObject, tile)
            configureMove(it, tile)
            configureJump(it, tile, gameObject)
            configureChest(it, tile,mapObject)
            configureState(it, tile, world, physicWorld, assetHelper)
            configureDamage(it, tile)
            configureInventory(it, gameObject)
            configureItem(it, gameObject)
            configureAttack(it, tile)
            configureStat(it, tile)
            configureLife(it, tile)
            configureAi(it, tile, physicWorld)
            configureShip(it, tile)
            logEntity(it, world, gameObject)
        }

    }

    private fun spawnStaticObject(mapObject: MapObject, x: Int, y: Int) {
        val body = physicWorld.createBody(StaticBody, vec2(x.toFloat(), y.toFloat()))
        val fixtureDefUserData = fixtureDefinitionOf(mapObject, true)
        body.createFixtures(listOf(fixtureDefUserData))
    }

    private fun spawnBodilessEntities(
        mapObject: TiledMapTileMapObject,
        tile: TiledMapTile,
        gameObject: GameObject,
    ) {
        world.entity {
            val position = vec2(mapObject.x * UNIT_SCALE, mapObject.y * UNIT_SCALE)
            configureEntityGraphic(it, mapObject, position, gameObject, assetHelper, world, 0f)
            configureEntityTags(it, mapObject, tile)
            logEntity(it, world, gameObject)
        }
    }

    companion object {
        fun logEntities(world: com.github.quillraven.fleks.World) {
            world.snapshot().forEach { entity, snapshot ->
                val components =
                    snapshot.components.mapNotNull { it::class.simpleName }.toMutableList()
                Gdx.app.log(
                    "EntityDebug",
                    "Entity ${entity.id} components: ${components.joinToString(", ")}"
                )
            }
        }

        fun logEntity(
            entity: Entity,
            world: com.github.quillraven.fleks.World,
            gameObject: GameObject? = null,
        ) = with(world) {
            val snapshot = world.snapshot()[entity] ?: return@with
            val components = snapshot.components.mapNotNull { it::class.simpleName }.toMutableList()
            if (gameObject != null) components.add(gameObject.name)
            Gdx.app.log(
                "EntityDebug",
                "Entity ${entity.id} components: ${components.joinToString(", ")}"
            )
        }

    }

    fun dispose() {
        OBJECT_FIXTURES.values.forEach { fixture ->
            fixture.forEach {
                it.fixtureDef.shape.disposeSafely()
            }
        }
        OBJECT_FIXTURES.clear()
    }
}
