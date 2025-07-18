package com.libgdx.treasurehunter.utils

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.ChainShape
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.Shape
import kotlin.math.max
import kotlin.math.min

fun mirrorVertices(shape: Shape, offset: Vector2): Array<Vector2> {
    val vertexCount = when (shape) {
        is ChainShape -> shape.vertexCount
        is PolygonShape -> shape.vertexCount
        else -> throw IllegalArgumentException("Unsupported shape type")
    }
    return Array(vertexCount) { i ->
        val vertex = Vector2()
        when (shape) {
            is ChainShape -> shape.getVertex(i, vertex)
            is PolygonShape -> shape.getVertex(i, vertex)
        }
        Vector2(-vertex.x + offset.x, vertex.y + offset.y)
    }
}

fun ChainShape.mirror(offset: Vector2): ChainShape {
    val mirrored = mirrorVertices(this, offset)
    return ChainShape().apply { createChain(mirrored) }
}

fun PolygonShape.mirror(offset: Vector2): PolygonShape {
    val mirrored = mirrorVertices(this, offset)
    return PolygonShape().apply { set(mirrored) }
}



fun offsetVertices(shape: Shape, offset: Vector2): Array<Vector2> {
    val vertexCount = when (shape) {
        is ChainShape -> shape.vertexCount
        is PolygonShape -> shape.vertexCount
        else -> throw IllegalArgumentException("Unsupported shape type")
    }
    return Array(vertexCount) { i ->
        val vertex = Vector2()
        when (shape) {
            is ChainShape -> shape.getVertex(i, vertex)
            is PolygonShape -> shape.getVertex(i, vertex)
        }
        Vector2(vertex.x + offset.x, vertex.y + offset.y)
    }
}

fun ChainShape.offset(offset: Vector2): ChainShape {
    val offsetted = offsetVertices(this, offset)
    return ChainShape().apply { createChain(offsetted) }
}

fun PolygonShape.offset(offset: Vector2): PolygonShape {
    val offsetted = offsetVertices(this, offset)
    return PolygonShape().apply { set(offsetted) }
}


fun ChainShape.width(): Float {
    var minX = Float.MAX_VALUE
    var maxX = -Float.MAX_VALUE

    val vertex = Vector2()
    for (i in 0 until vertexCount) {
        getVertex(i, vertex)
        if (vertex.x < minX) minX = vertex.x
        if (vertex.x > maxX) maxX = vertex.x
    }

    return maxX - minX
}


fun ChainShape.vertices(): FloatArray {
    val vertexCount = this.vertexCount
    val vertices = FloatArray(vertexCount * 2)

    for (i in 0 until vertexCount) {
        val vertex = Vector2()
        this.getVertex(i, vertex)
        vertices[i * 2] = vertex.x
        vertices[i * 2 + 1] = vertex.y
    }

    return vertices
}

fun ChainShape.calculateEffectPosition(): Vector2 {
    val center = Vector2()
    var minX = Float.MAX_VALUE
    var maxX = Float.MIN_VALUE
    var minY = Float.MAX_VALUE
    var maxY = Float.MIN_VALUE

    for (i in 0 until this.vertexCount) {
        val vertex = Vector2()
        this.getVertex(i, vertex)

        minX = min(minX, vertex.x)
        maxX = max(maxX, vertex.x)
        minY = min(minY, vertex.y)
        maxY = max(maxY, vertex.y)
    }

    center.x = (minX + maxX) / 2
    center.y = (minY + maxY) / 2

    return center
}

fun ChainShape.getCentroid(): Vector2 {
    val vertexCount = this.vertexCount
    val centroid = Vector2()
    val temp = Vector2()
    for (i in 0 until vertexCount) {
        this.getVertex(i, temp)
        centroid.add(temp)
    }
    return centroid.scl(1f / vertexCount.toFloat())
}

fun FloatArray.mirrorVertices(): FloatArray {
    val mirroredVertices = FloatArray(this.size)
    for (i in this.indices step 2) {
        mirroredVertices[i] = -this[i]
        mirroredVertices[i + 1] = this[i + 1]
    }
    return mirroredVertices
}



