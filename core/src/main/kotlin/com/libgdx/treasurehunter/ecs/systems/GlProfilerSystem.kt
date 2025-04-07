package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.github.quillraven.fleks.IntervalSystem
import com.github.quillraven.fleks.World.Companion.inject
import com.libgdx.treasurehunter.game.PhysicWorld

class GlProfilerSystem(
    private val physicWorld : PhysicWorld = inject()
) : IntervalSystem() {

    private val glProfiler by lazy {
        GLProfiler(Gdx.graphics).apply {
            enable()
        }
    }

    override fun onTick() {
        Gdx.graphics.setTitle(
            "FPS: ${Gdx.graphics.framesPerSecond} " +
            "| Draw Calls: ${glProfiler.drawCalls} " +
            "| Texture Bindings: ${glProfiler.textureBindings} " +
            "| Shader Switches: ${glProfiler.shaderSwitches} " +
            "| Body Count: ${physicWorld.bodyCount} " +
            "| Fixture Count: ${physicWorld.fixtureCount} " +
            "| Entity Count: ${world.numEntities} " 
        )
        glProfiler.reset()
    }
}
