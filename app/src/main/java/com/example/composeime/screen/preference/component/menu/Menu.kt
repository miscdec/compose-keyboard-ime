package com.example.composeime.screen.preference.component.menu


import androidx.annotation.StringRes
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun SettingsMenu(
    expanded: Boolean,
    options: List<Menu>,
    onClick: () -> Unit
) = DropdownMenu(
    expanded = expanded,
    onDismissRequest = onClick,
    offset = DpOffset(0.dp, 5.dp),
) {


    options.forEach {
        MenuItem(
            value = it,
            onClick = onClick
        )
    }
}

@Composable
private fun MenuItem(
    value: Menu,
    onClick: () -> Unit
) = DropdownMenuItem(
    text = { Text(text = stringResource(id = value.label)) },
    onClick = onClick
)

enum class Menu(
    @StringRes val label: Int,
    val reason: String,
) {

}
