package com.example.composeime.keyboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Scoreboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.example.composeime.ime.platform.android.operation.onKeyPress
import com.example.composeime.ime.platform.android.service.IMEService
import com.example.composeime.keyboard.common.model.Key
import com.example.composeime.keyboard.common.model.Keyboard
import com.example.composeime.keyboard.layouts.numberKeyboard
import com.example.composeime.keyboard.layouts.standardKeyboard
import com.example.composeime.ui.components.KeyboardLayout
import com.example.composeime.ui.components.keyboard.Keyboard

@Composable
fun KeyboardScreen() {
    val ctx = LocalContext.current
    val keyboards = listOf(standardKeyboard, numberKeyboard)
//    val keyboardIndex  by remember { mutableIntStateOf(0) }
    var currentKeyboard by remember { mutableStateOf(keyboards[0]) }

    val onKeyPress = onKeyPress(ctx, onSwitch = {

    }, onChangeMode = {
        currentKeyboard = keyboards[1]
    })

    val barContent: @Composable () -> Unit = {

//        LazyRow {
////            items(barIconList) {
////                IconButton(onClick = {
////
////                }) {
////                    Icon(imageVector = it.icon, contentDescription = null)
////                }
////            }
//        }
    }
    KeyboardLayout(
        keyboardContent = {
            val ime = (ctx as IMEService)
            val editorinfo = ime.currentInputEditorInfo
            val connection = ime.currentInputConnection
            val selstart by remember { mutableStateOf(editorinfo.initialSelStart.toString()) }
            val selend by remember { mutableStateOf(editorinfo.initialSelEnd.toString()) }
            Column {
                Text(text = connection.getSelectedText(0)?.toString() ?: "")
                Text(text = editorinfo.packageName)
                Text(text = selstart)
                Text(text = selend)
                Text(text = ime.currentInputConnection.getCursorCapsMode(0).toString())
                Text(
                    text = "${connection.getTextBeforeCursor(8, 0)}${
                        connection.getTextAfterCursor(
                            8,
                            0
                        )
                    }"
                )
                // 在 Composable 中使用状态
            }
        },
        barContent = barContent,
        list = listOf("1", "2", "3"),
        selectedText = "1",
        onSelected = {

        }
    ) {

    }
}


sealed class BarIcon(
    val icon: ImageVector,
    val description: String? = null,
    val route: String,
) {

    data object Clipboard : BarIcon(
        icon = Icons.Default.Scoreboard,
        description = "",
        route = ""
    )

    data object Setting : BarIcon(
        icon = Icons.Default.Settings,
        description = "",
        route = ""
    )

    data object Emoji : BarIcon(
        icon = Icons.Default.EmojiEmotions,
        description = "",
        route = ""
    )


}

val barIconList = listOf(BarIcon.Clipboard, BarIcon.Setting, BarIcon.Emoji)