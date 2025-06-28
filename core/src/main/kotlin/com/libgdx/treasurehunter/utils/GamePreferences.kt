package com.libgdx.treasurehunter.utils

import com.badlogic.gdx.Preferences
import com.libgdx.treasurehunter.event.GameEvent.AudioChangeEvent
import com.libgdx.treasurehunter.event.GameEventDispatcher

class GamePreferences (
    private val preferences: Preferences
){
    val musicVolume : Float
        get() = preferences.getFloat("musicVolume", 0f)
    val soundVolume : Float
        get() =  preferences.getFloat("soundVolume", 0f)
    val muteMusic : Boolean
        get() = preferences.getBoolean("muteMusic", false)
    val muteSound : Boolean
        get() = preferences.getBoolean("muteSound", false)

    fun storeMusicVolume(volume: Float) {
        preferences.putFloat("musicVolume", volume)
        preferences.flush()
        fireAudioEvent()

    }
    fun storeSoundVolume(volume: Float) {
        preferences.putFloat("soundVolume", volume)
        preferences.flush()
        fireAudioEvent()
    }
    fun storeMuteMusic(mute: Boolean) {
        preferences.putBoolean("muteMusic", mute)
        preferences.flush()
        fireAudioEvent()
    }
    fun storeMuteSound(mute: Boolean) {
        preferences.putBoolean("muteSound", mute)
        preferences.flush()
        fireAudioEvent()
    }
    private fun fireAudioEvent(){
        GameEventDispatcher.fireEvent(AudioChangeEvent(
            soundVolume,
            musicVolume,
            muteMusic,
            muteSound
        ))
    }

}
