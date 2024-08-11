package com.example.composeime.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composeime.ui.theme.keyBackgroundColor

/**
 * Am 2021-01-12 erstellt.
 */

@Composable
fun KeyboardBar(
    modifier: Modifier = Modifier,
    selected: String,
    onSelected: (String) -> Unit,
    barContent: @Composable () -> Unit,
    list: List<String>? = null,
) {
    if (list == null) {
        barContent()
    } else {
        LazyRow(
            modifier = modifier
                .background(keyBackgroundColor),
            contentPadding = PaddingValues(start = 8.dp, end = 8.dp)
        ) {
            items(list) { font ->
                FontItem(label = font, onClick = { onSelected(font) }, selected = font == selected)
            }
        }
    }

}