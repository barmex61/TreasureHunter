package com.libgdx.treasurehunter.utils

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.ChainShape
import kotlin.math.max
import kotlin.math.min
import kotlin.times

fun mirrorChainShape(chainShape: ChainShape,offset : Vector2) : ChainShape{
    val vertexCount = chainShape.vertexCount
    val vertices = Array(vertexCount) { Vector2() }

    for (i in 0 until vertexCount) {
        val vertex = Vector2()
        chainShape.getVertex(i, vertex)
        vertices[i] = Vector2(-vertex.x + offset.x, vertex.y + offset.y)
    }

    return ChainShape().apply {
        createChain(vertices)
    }
}

private fun calculateShapeWidth(chainShape: ChainShape): Float {
    var minX = Float.MAX_VALUE
    var maxX = Float.MIN_VALUE
    val tempVertex = Vector2()

    for (i in 0 until chainShape.vertexCount) {
        chainShape.getVertex(i, tempVertex)
        minX = minOf(minX, tempVertex.x)
        maxX = maxOf(maxX, tempVertex.x)
    }

    return maxX - minX
}

fun ChainShape.offset(offset: Vector2) : ChainShape {
    val vertexCount = vertexCount
    val vertices = Array(vertexCount) { Vector2() }

    for (i in 0 until vertexCount) {
        val vertex = Vector2()
        getVertex(i, vertex)
        vertices[i] = Vector2(vertex.x + offset.x, vertex.y + offset.y)
    }

    return ChainShape().apply {
        createChain(vertices)
    }
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



