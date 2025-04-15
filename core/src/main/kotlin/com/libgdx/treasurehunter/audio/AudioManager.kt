package com.libgdx.treasurehunter.audio

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventListener
import ktx.tiled.property

class AudioManager : GameEventListener {

    private val musicCache : MutableMap<String, Music> = mutableMapOf()

    override fun onEvent(event: GameEvent) {
        when(event){
            is GameEvent.MapChangeEvent ->{
                val mapMusicPath = event.tiledMap.property<String>("mapMusic")
                val mapMusicVolume = event.tiledMap.property<Float>("mapMusicVolume")
                val ambientMusicPath = event.tiledMap.property<String>("ambientMusic")
                val ambientMusicVolume = event.tiledMap.property<Float>("ambientMusicVolume")
                val musicPath =  if (mapMusicPath.isNotEmpty()) mapMusicPath else ambientMusicPath
                val musicVolume = if (mapMusicVolume > 0) mapMusicVolume else ambientMusicVolume
                val music = musicCache.getOrPut(musicPath) {
                    Gdx.audio.newMusic(Gdx.files.internal(musicPath)).apply {
                        isLooping = true
                        volume = musicVolume
                    }
                }
                music.play()
            }
            else -> Unit
        }
    }
}
