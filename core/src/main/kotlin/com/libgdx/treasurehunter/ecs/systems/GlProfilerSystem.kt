package com.libgdx.treasurehunter.ecs.systems

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.profiling.GLProfiler
import com.github.quillraven.fleks.IntervalSystem

class GlProfilerSystem : IntervalSystem() {


    private val glProfiler by lazy {
        GLProfiler(Gdx.graphics).apply {
            enable()
        }
    }


    override fun onTick() {
        Gdx.graphics.setTitle(
            "FPS: ${Gdx.graphics.framesPerSecond} | Draw Calls: ${glProfiler.drawCalls} | Texture Bindings: ${glProfiler.textureBindings} | Shader Switches: ${glProfiler.shaderSwitches}"
        )
        glProfiler.reset()
    }
}
