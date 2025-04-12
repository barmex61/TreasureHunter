package com.libgdx.treasurehunter.utils

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.ChainShape
import kotlin.math.max
import kotlin.math.min

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


