package com.example.composeime.screen.preference.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties

@Composable
fun DropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(15.dp),
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    properties: PopupProperties = PopupProperties(focusable = true),
    content: @Composable ColumnScope.() -> Unit
) = CustomMenuShape(shape) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        offset = offset,
        properties = properties,
        content = content
    )
}

@Composable
fun DropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(15.dp),
    contentAlignment: Alignment = Alignment.TopStart,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    properties: PopupProperties = PopupProperties(focusable = true),
    surface: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) = Box {
    surface()
    CustomMenuShape(shape) {
        Box(
            modifier = Modifier
                .align(contentAlignment),
            contentAlignment = contentAlignment
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest,
                modifier = modifier,
                offset = offset,
                properties = properties,
                content = content
            )
        }
    }
}

@Composable
private fun CustomMenuShape(
    shape: RoundedCornerShape,
    content: @Composable () -> Unit
) = MaterialTheme(
    shapes = MaterialTheme.shapes.copy(extraSmall = shape),
    content = content
)