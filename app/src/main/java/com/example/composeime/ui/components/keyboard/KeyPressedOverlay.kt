package com.example.composeime.ui.components.keyboard

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composeime.R
import com.example.composeime.keyboard.common.model.Key
import com.example.composeime.ui.theme.keyBackgroundColor

@Composable
fun KeyOverlay(modifier: Modifier = Modifier, label: Key.Label) {
    val shape = RoundedCornerShape(6.dp)
    KeyLabel(
        modifier = modifier.shadow(4.dp, shape = shape),
        paddingTop = 16.dp,
        paddingBottom = 48.dp,
        label = label,
        shape = shape,
        backgroundColor = keyBackgroundColor
    )
}

@Composable
@Preview
fun KeyOverlayPreview() {
    KeyLabel(
        modifier = Modifier.shadow(4.dp, shape = RoundedCornerShape(6.dp)),
        paddingTop = 16.dp,
        paddingBottom = 48.dp,
        label = Key.Label.Text("A"),
        shape = RoundedCornerShape(6.dp),
        backgroundColor = keyBackgroundColor
    )
}

@Composable
@Preview
fun KeyOverlayIconPreview() {
    KeyLabel(
        modifier = Modifier.shadow(4.dp, shape = RoundedCornerShape(6.dp)),
        paddingTop = 16.dp,
        paddingBottom = 48.dp,
        label = Key.Label.Icon(R.drawable.ic_keyboard_confirm),
        shape = RoundedCornerShape(6.dp),
        backgroundColor = keyBackgroundColor
    )
}