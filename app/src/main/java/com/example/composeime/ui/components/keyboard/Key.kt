package com.example.composeime.ui.components.keyboard

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.composeime.keyboard.common.model.Key
import com.example.composeime.ui.theme.keyBackgroundColor
import kotlin.math.roundToInt


@Composable
fun KeyBox(
    modifier: Modifier = Modifier,
    key: Key,
    popup: Boolean,
    onClick: (Key) -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val shape = RoundedCornerShape(4.dp)
    val offsetX = remember { mutableFloatStateOf(0f) }
    val offsetY = remember { mutableFloatStateOf(0f) }
    var size by remember { mutableStateOf(Size.Zero) }
    KeyLabel(
        modifier = modifier
            .offset { (IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt())) }
            .padding(2.dp)
            .clip(shape)
            .clickable(
                onClick = { onClick(key) },
                indication = LocalIndication.current,
                interactionSource = interactionSource
            ),
        shape = shape,
        paddingTop = 12.dp,
        paddingBottom = 12.dp,
        label = key.label,
        backgroundColor = when (key.background) {
            Key.Background.TRANSPARENT -> Color.Transparent
            Key.Background.FILLED -> keyBackgroundColor
        }
    )
}
