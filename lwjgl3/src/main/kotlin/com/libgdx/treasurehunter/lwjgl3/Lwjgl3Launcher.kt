@file:JvmName("Lwjgl3Launcher")

package com.libgdx.treasurehunter.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.libgdx.treasurehunter.game.TreasureHunter

/** Launches the desktop (LWJGL3) application. */
fun main() {
    // This handles macOS support and helps on Windows.
    if (StartupHelper.startNewJvmIfRequired())
      return
    Lwjgl3Application(TreasureHunter(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("TreasureHunter")
        setWindowedMode(640, 480)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
        useVsync(false)
    })
}
