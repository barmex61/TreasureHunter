package com.libgdx.treasurehunter.tiled

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.MapLayer
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
import com.libgdx.treasurehunter.ecs.components.Graphic
import com.libgdx.treasurehunter.ecs.components.Physic
import com.libgdx.treasurehunter.enums.AssetHelper
import com.libgdx.treasurehunter.utils.Constants.OBJECT_FIXTURES
import com.libgdx.treasurehunter.utils.GameObject
import ktx.app.gdxError
import ktx.assets.disposeSafely
import ktx.box2d.body
import ktx.math.vec2
import ktx.tiled.id
import ktx.tiled.property
import ktx.tiled.x
import ktx.tiled.y
import kotlin.div
import kotlin.math.sin
import kotlin.times


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
        val body = createBody(BodyType.valueOf(bodyType), vec2(x,y),rotation).apply {
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
            /*
            configureDamage(it,tile)
            configureLife(it,tile) */
        }

    }

    private fun spawnStaticObject(mapObject: MapObject,x: Int,y : Int){
        val body = createBody(StaticBody,vec2(x.toFloat(),y.toFloat()))
        val fixtureDefUserData = fixtureDefinitionOf(mapObject)
        body.createFixtures(listOf(fixtureDefUserData))
    }

    private fun createBody(bodyType : BodyType,position: Vector2,rotation : Float = 0f,gravityScale : Float = 1f) = physicWorld.body(bodyType) {
        this.position.set(position)
        if (rotation != 0f){
            this.fixedRotation = false
        }
        if (gravityScale != 1f){
            this.gravityScale = gravityScale
        }
    }

    private fun Body.createFixtures(fixtureDefUserData: List<FixtureDefUserData>) {
        fixtureDefUserData.forEach { it ->
            createFixture(it.fixtureDef).apply {
                this.userData = it.userData
            }
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

    companion object{

        data class FixtureDefUserData(val fixtureDef: FixtureDef, val userData : String)

        fun fixtureDefinitionOf(mapObject: MapObject): FixtureDefUserData {
            val fixtureDef = when(mapObject){
                is RectangleMapObject -> rectangleFixtureDef(mapObject)
                is EllipseMapObject -> ellipseFixtureDef(mapObject)
                is PolygonMapObject -> polygonFixtureDef(mapObject)
                is PolylineMapObject -> polylineFixtureDef(mapObject)
                else -> gdxError("Unsupported mapObject $mapObject")
            }
            val userData = mapObject.property("userData","")
            fixtureDef.apply {
                friction = mapObject.property("friction",0f)
                restitution = mapObject.property("restitution",0f)
                isSensor = mapObject.property("isSensor",false)
                density = mapObject.property("density",0f)
            }
            return FixtureDefUserData(fixtureDef,userData)
        }


        private fun polylineFixtureDef(mapObject: PolylineMapObject) : FixtureDef {
            return polygonFixtureDef(mapObject.x,mapObject.y,mapObject.polyline.vertices,false)
        }

        private fun polygonFixtureDef(mapObject: PolygonMapObject) : FixtureDef {
            return polygonFixtureDef(mapObject.x,mapObject.y,mapObject.polygon.vertices,true)
        }

        private fun polygonFixtureDef(polyX : Float,polyY : Float,polyVertices : FloatArray,loop : Boolean) : FixtureDef {
            val x = polyX * UNIT_SCALE
            val y = polyY * UNIT_SCALE
            val vertices = FloatArray(polyVertices.size){vertexIdx ->
                if (vertexIdx % 2 == 0){
                    x + polyVertices[vertexIdx] * UNIT_SCALE
                }else{
                    y + polyVertices[vertexIdx] * UNIT_SCALE
                }
            }

            return FixtureDef().apply {
                shape =
                    ChainShape().apply {
                        if (loop){
                            createLoop(vertices)
                        }else{
                            createChain(vertices)
                        }
                    }
            }
        }

        fun rectangleFixtureDef(mapObject: RectangleMapObject) : FixtureDef{
            val(rectX,rectY,rectW,rectH) = mapObject.rectangle
            val boxW = rectW * UNIT_SCALE / 2f
            val boxH = rectH * UNIT_SCALE / 2f
            return FixtureDef().apply {
                shape = PolygonShape().apply {
                    setAsBox(boxW,boxH, vec2(rectX * UNIT_SCALE + boxW,rectY * UNIT_SCALE + boxH),0f)
                }
            }
        }

        private fun ellipseFixtureDef(mapObject: EllipseMapObject) : FixtureDef {
            val (x,y,w,h) = mapObject.ellipse
            val ellipseX = x * UNIT_SCALE
            val ellipseY = y * UNIT_SCALE
            val ellipseW = w * UNIT_SCALE / 2f
            val ellipseH = h * UNIT_SCALE / 2f
            return if (MathUtils.isEqual(ellipseW, ellipseH, 0.1f)){
                FixtureDef().apply {
                    shape = CircleShape().apply {
                        position = vec2(ellipseX + ellipseW , ellipseY + ellipseH)
                        radius = ellipseW
                    }
                }
            }else{
                val numVertices = 20
                val angleStep = MathUtils.PI2 / numVertices
                val vertices = Array(numVertices){vertexIdx ->
                    val angle = vertexIdx * angleStep
                    val offsetX = ellipseW * MathUtils.cos(angle)
                    val offsetY = ellipseH * sin(angle)
                    vec2(ellipseX + ellipseW + offsetX,ellipseY + ellipseH + offsetY)
                }
                FixtureDef().apply {
                    shape = ChainShape().apply {
                        createLoop(vertices)
                    }
                }
            }
        }
    }

}
