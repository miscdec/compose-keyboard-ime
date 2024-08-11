package com.example.composeime.keyboard.common.dsl

import com.example.composeime.keyboard.common.model.KeyRow
import com.example.composeime.keyboard.common.model.Keyboard

/**
 * Am 2021-01-05 erstellt.
 */

class KeyboardBuilder : Builder<Keyboard, KeyRow>() {

    override fun create(elements: List<KeyRow>): Keyboard =
        Keyboard(elements)
}