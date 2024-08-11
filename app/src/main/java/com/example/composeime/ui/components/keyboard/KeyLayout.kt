package com.example.composeime.ui.components.keyboard

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.composeime.keyboard.common.model.Key

/**
 * Am 2021-01-04 erstellt.
 */

@Composable
fun KeyLayout(
    key: Key,
    modifier: Modifier,
    modifierPressed: Modifier,
    onClick: (Key) -> Unit
) {
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    KeyBox(
        modifier = modifier,
        key = key,
        popup = key.animation == Key.Animation.POPUP,
        onClick = onClick,
        interactionSource = interactionSource,
    )
    if (isPressed && key.animation == Key.Animation.POPUP) KeyOverlay(
        modifier = modifierPressed,
        label = key.label
    )


}