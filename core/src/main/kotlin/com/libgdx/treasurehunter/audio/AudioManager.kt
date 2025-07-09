package com.libgdx.treasurehunter.audio

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.libgdx.treasurehunter.event.GameEvent
import com.libgdx.treasurehunter.event.GameEventListener
import com.libgdx.treasurehunter.utils.GamePreferences
import com.libgdx.treasurehunter.utils.GameProperties
import ktx.tiled.property

class AudioManager (
    var soundVolume : Float ,
    var musicVolume : Float ,
    var muteMusic : Boolean ,
    var muteSound : Boolean ,
): GameEventListener {

    private val musicCache : MutableMap<String, Music> = mutableMapOf()
    private val soundCache : MutableMap<String, Sound> = mutableMapOf()

    fun playSound(soundPath: String, volume: Float) {
        if (muteSound) return

        val sound = soundCache.getOrPut(soundPath) {
            Gdx.audio.newSound(Gdx.files.internal(soundPath))
        }
        sound.stop()
        sound.play(volume * soundVolume)
    }

    fun dispose() {
        musicCache.values.forEach { it.dispose() }
        soundCache.values.forEach { it.dispose() }
        musicCache.clear()
        soundCache.clear()
    }

    override fun onEvent(event: GameEvent) {
        when(event){
            is GameEvent.MapChangeEvent ->{
                val mapMusicPath = event.tiledMap.property<String>("mapMusic")
                val ambientMusicPath = event.tiledMap.property<String>("ambientMusic")
                val musicPath =  if (mapMusicPath.isNotEmpty()) mapMusicPath else ambientMusicPath
                val music = musicCache.getOrPut(musicPath) {
                    Gdx.audio.newMusic(Gdx.files.internal(musicPath)).apply {
                        isLooping = true
                        volume = musicVolume
                    }
                }
                music.play()
            }
            is GameEvent.AudioChangeEvent ->{
                soundVolume = if (muteSound) 0f else event.soundVolume
                musicVolume = if (muteMusic) 0f else event.musicVolume
                musicCache.values.forEach { it.volume = musicVolume }
            }
            is GameEvent.PlaySoundEvent -> {
                playSound(event.soundAsset.path,event.soundAsset.volume)
            }
            else -> Unit
        }
    }
}


