package com.libgdx.treasurehunter.utils

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.CircleMapObject
import com.badlogic.gdx.maps.objects.EllipseMapObject
import com.badlogic.gdx.maps.objects.PolygonMapObject
import com.badlogic.gdx.maps.objects.PolylineMapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.Filter
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.Shape
import com.libgdx.treasurehunter.game.PhysicWorld
import com.libgdx.treasurehunter.utils.Constants.UNIT_SCALE

import ktx.app.gdxError
import ktx.box2d.body
import ktx.math.vec2
import ktx.tiled.property
import ktx.tiled.shape
import ktx.tiled.x
import ktx.tiled.y
import kotlin.collections.forEach
import kotlin.math.sin


fun PhysicWorld.createBody(bodyType : BodyType,position: Vector2,rotation : Float = 0f,gravityScale : Float = 1f) = this.body(bodyType) {
    this.position.set(position)
    if (rotation != 0f){
        this.fixedRotation = false
    }
    if (gravityScale != 1f){
        this.gravityScale = gravityScale
    }
}

fun Body.createFixtures(fixtureDefUserData: List<FixtureDefUserData>) {
    fixtureDefUserData.forEach { it ->
        createFixture(it.fixtureDef).apply {
            this.userData = it.userData
        }
    }
}

data class FixtureDefUserData(val fixtureDef: FixtureDef, val userData : String)

fun fixtureDefinitionOf(mapObject: MapObject,isGround : Boolean = false): FixtureDefUserData {
    val fixtureDef = when(mapObject){
        is RectangleMapObject -> rectangleFixtureDef(mapObject)
        is EllipseMapObject -> ellipseFixtureDef(mapObject)
        is PolygonMapObject -> polygonFixtureDef(mapObject)
        is PolylineMapObject -> polylineFixtureDef(mapObject)
        else -> gdxError("Unsupported mapObject $mapObject")
    }
    val userData = mapObject.property("userData","")
    fixtureDef.apply {
        friction = mapObject.property("friction",if (isGround) 0.2f else 0f)
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


fun ellipseFixtureDef(mapObject: EllipseMapObject) : FixtureDef {
    val sensorCircleRadius = mapObject.property("radius",0f)
    val (x,y,w,h) = mapObject.ellipse
    val ellipseX = x * UNIT_SCALE
    val ellipseY = y * UNIT_SCALE
    val ellipseW = if (sensorCircleRadius == 0f) w * UNIT_SCALE / 2f else sensorCircleRadius
    val ellipseH = if (sensorCircleRadius == 0f) h * UNIT_SCALE / 2f else sensorCircleRadius
    return if (MathUtils.isEqual(ellipseW, ellipseH, 0.1f) || sensorCircleRadius != 0f ){
        FixtureDef().apply {
            shape = CircleShape().apply {
                position = vec2(ellipseX + 0.3f,ellipseY + 0.3f)
                radius = ellipseW
            }
            this.isSensor = isSensor
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

fun Body.destroyFixtures(){
    val fixturesToDestroy = mutableListOf<Fixture>()
    this.fixtureList.forEach {fixture ->
        fixturesToDestroy.add(fixture)
    }
    fixturesToDestroy.forEach { fixture ->
        this.destroyFixture(fixture)
    }
    fixturesToDestroy.clear()
}

fun FixtureDef.copy(
    isSensor : Boolean? = null,
    density : Float? = null,
    friction : Float? = null,
    restitution : Float? = null,
    filter : Filter? = null,
    shape : Shape? = null
) : FixtureDef{
   return  return FixtureDef().also {
       it.isSensor = isSensor ?: this.isSensor
       it.density = density ?: this.density
       it.friction = friction ?: this.friction
       it.restitution = restitution ?: this.restitution
       it.filter.set(filter ?: this.filter)
       it.shape = shape ?: this.shape
   }
}
