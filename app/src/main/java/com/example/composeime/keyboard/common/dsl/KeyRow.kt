package com.example.composeime.keyboard.common.dsl

import com.example.composeime.keyboard.common.model.Key


/**
 * Am 2021-01-05 erstellt.
 */

fun KeyboardBuilder.row(
    width: Float = 1.0f,
    builder: KeyRowBuilder.() -> Unit
) {
    val scope = KeyRowBuilder(width)
    scope.builder()
    this.add(scope.build())
}

fun KeyboardBuilder.row(vararg elements: String, width: Float = 1.0f) {
    val scope = KeyRowBuilder(width)
    elements.forEach {
        scope.add(
            KeyRowBuilder.Key(
                label = Key.Label.Text(it),
                action = Key.Action.Input(it),
                absoluteWeight = 1f
            )
        )
    }
    this.add(scope.build())
}
