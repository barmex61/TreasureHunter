package com.libgdx.treasurehunter.utils

import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.math.Ellipse
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2

operator fun Rectangle.component1() = this.x
operator fun Rectangle.component2() = this.y
operator fun Rectangle.component3() = this.width
operator fun Rectangle.component4() = this.height

operator fun Ellipse.component1() = this.x
operator fun Ellipse.component2() = this.y
operator fun Ellipse.component3() = this.width
operator fun Ellipse.component4() = this.height

operator fun Circle.component1() = this.x
operator fun Circle.component2() = this.y
operator fun Circle.component3() = this.radius

operator fun Vector2.component1() = this.x
operator fun Vector2.component2() = this.y

operator fun Vector2.times(scalar: Float) = Vector2(this.x * scalar, this.y * scalar)
operator fun Vector2.plus(vector: Vector2) = Vector2(this.x + vector.x, this.y + vector.y)
operator fun Vector2.plus(scalar : Float) = Vector2(this.x + scalar, this.y + scalar)
operator fun Vector2.minus(scalar : Float) = Vector2(this.x - scalar, this.y - scalar)
