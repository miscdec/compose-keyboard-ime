package com.example.composeime.keyboard

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView
import com.example.composeime.ui.theme.ComposeIMETheme

class ComposeKeyboardView(context: Context) : AbstractComposeView(context) {

    @Composable
    override fun Content() {
        Surface {
            ComposeIMETheme(darkTheme = isSystemInDarkTheme()) {
                KeyboardScreen()
            }
        }
    }
}

