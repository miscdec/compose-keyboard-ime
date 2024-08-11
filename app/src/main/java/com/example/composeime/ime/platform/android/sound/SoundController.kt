package com.example.composeime.ime.platform.android.sound

import android.media.AudioManager
import splitties.systemservices.audioManager


class SoundControllerImpl : SoundController {

    private var audioController: AudioManager = audioManager

    override fun playSoundEffect(effectType: EffectType) {
        audioController.playSoundEffect(effectType.type)
    }
}