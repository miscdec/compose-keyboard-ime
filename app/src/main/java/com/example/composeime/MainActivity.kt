package com.example.composeime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.example.composeime.route.AppNavHost
import com.example.composeime.ui.theme.ComposeIMETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainWindow()
        }
    }
}

@Composable
fun MainWindow() {
    ComposeIMETheme(darkTheme = isSystemInDarkTheme()) {
        Surface {
            AppNavHost()
        }
    }
}

