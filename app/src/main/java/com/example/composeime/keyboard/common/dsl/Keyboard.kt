package com.example.composeime.keyboard.common.dsl

import com.example.composeime.keyboard.common.model.Keyboard

/**
 * Am 2021-01-05 erstellt.
 */

fun keyboard(builder: KeyboardBuilder.() -> Unit): Keyboard {
    val scope = KeyboardBuilder()
    scope.builder()
    return scope.build()
}