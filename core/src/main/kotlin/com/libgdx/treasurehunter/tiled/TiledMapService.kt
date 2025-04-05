package com.libgdx.treasurehunter.tiled

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.StaticBody
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.libgdx.treasurehunter.utils.component1
import com.libgdx.treasurehunter.utils.component2
import com.libgdx.treasurehunter.utils.component3
import com.libgdx.treasurehunter.utils.component4
import com.badlogic.gdx.physics.box2d.World
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventListener
import com.libgdx.treasurehunter.utils.Constants.UNIT_SCALE
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.CircleShape
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.utils.Constants.OBJECT_FIXTURES
import com.libgdx.treasurehunter.utils.GameObject
import com.libgdx.treasurehunter.utils.createBody
import com.libgdx.treasurehunter.utils.createFixtures
import com.libgdx.treasurehunter.utils.fixtureDefinitionOf
import ktx.app.gdxError
import ktx.assets.disposeSafely
import ktx.box2d.body
import ktx.math.vec2
import ktx.tiled.property
import ktx.tiled.x
import ktx.tiled.y
import kotlin.math.sin


class TiledMapService (
    private val physicWorld : World,
    private val world: com.github.quillraven.fleks.World,
    private val assetHelper: AssetHelper
) : GameEventListener {


    override fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.MapChangeEvent -> {
                spawnEntities(event.tiledMap)
            }
        }
    }

    private fun TiledMapTileLayer.forEachCell(onCell: (TiledMapTileLayer.Cell, Int, Int) -> Unit){
        val width = this.width
        val height = this.height
        (0..width).forEach { x ->
            (0..height).forEach { y ->
                this.getCell(x,y)?.let { cell ->
                    onCell(cell,x,y)
                }
            }
        }
    }

    private fun spawnEntities(tiledMap: TiledMap){
        // spawn static ground bodies
        tiledMap.layers.filterIsInstance<TiledMapTileLayer>().forEach { tileLayer ->
            tileLayer.forEachCell{ cell,x,y ->
                cell.tile.objects.forEach { mapObject ->
                    spawnStaticObject(mapObject,x,y)
                }
            }
        }

        //spawn dynamic bodies

        tiledMap.layers.filter { it !is TiledMapTileLayer }.forEach { mapLayer ->
            mapLayer.objects.forEach { mapObject ->
                spawnEntity(mapObject)
            }
        }
    }

    private fun spawnEntity(mapObject: MapObject){
        if (mapObject !is TiledMapTileMapObject){
            gdxError("mapObject of type $mapObject is not supported")
        }
        val tile = mapObject.tile
        val bodyType = tile.property<String>("bodyType","StaticBody")
        val gravityScale = tile.property<Float>("gravityScale",1f)
        val gameObjectStr = tile.property<String>("gameObject")
        val rotation = mapObject.rotation
        val gameObject = GameObject.valueOf(gameObjectStr)
        val fixtureDefUserData = OBJECT_FIXTURES[gameObject]?: gdxError("No fixture definitions for ${gameObject.atlasKey}")

        val x = mapObject.x * UNIT_SCALE
        val y = mapObject.y * UNIT_SCALE
        val body = physicWorld.createBody(BodyType.valueOf(bodyType), vec2(x,y),rotation).apply {
            this.gravityScale = gravityScale
            this.setTransform(this.position,-rotation * MathUtils.degreesToRadians)
        }
        body.createFixtures(fixtureDefUserData)
        world.entity {
            body.userData = it
            it += Physic(body)
            configureEntityGraphic(it,tile,body,gameObject, assetHelper,world,rotation)
            configureEntityTags(it,mapObject,tile)
            configureMove(it,tile)
            configureJump(it,tile)
            configureState(it,tile,world,physicWorld)
            configureAttack(it,tile)
            configureLife(it,tile)
        }

    }

    private fun spawnStaticObject(mapObject: MapObject,x: Int,y : Int){
        val body = physicWorld.createBody(StaticBody,vec2(x.toFloat(),y.toFloat()))
        val fixtureDefUserData = fixtureDefinitionOf(mapObject)
        body.createFixtures(listOf(fixtureDefUserData))
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
