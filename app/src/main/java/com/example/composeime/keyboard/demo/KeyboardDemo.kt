package com.example.composeime.keyboard.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.composeime.ime.platform.android.service.IMEService
import com.example.composeime.keyboard.demo.layout.FixedHeightBox

@Composable
fun KeyboardDemo() {
    val ctx = LocalContext.current
    val keysMatrix = arrayOf(
        arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
        arrayOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        arrayOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        arrayOf("Z", "X", "C", "V", "B", "N", "M")
    )

    Column(
        modifier = Modifier
            .background(Color(0xFF9575CD))
            .fillMaxWidth()
    ) {
        keysMatrix.forEach { row ->
            FixedHeightBox(modifier = Modifier.fillMaxWidth(), height = 56.dp) {
                Row(Modifier) {
                    row.forEach { key ->
                        KeyboardKey(
                            keyboardKey = key,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                (ctx as IMEService).currentInputConnection.commitText(
                                    key,
                                    key.length
                                )
                            })
                    }
                }
            }
        }
        FixedHeightBox(modifier = Modifier.fillMaxWidth(), height = 56.dp) {
            Row(Modifier) {
                KeyboardKey(
                    keyboardKey = "<-",
                    modifier = Modifier.weight(1f),
                    onClick = {
                        (ctx as IMEService).currentInputConnection.deleteSurroundingText(1, 0)
                    })

            }
        }
    }
}

