package com.example.composeime.ime.platform.android.sound

import android.media.AudioManager

sealed class EffectType {
    val type: Int = AudioManager.FX_KEYPRESS_STANDARD
}