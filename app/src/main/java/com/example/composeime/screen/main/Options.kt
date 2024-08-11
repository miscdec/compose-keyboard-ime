package com.example.composeime.screen.main

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.composeime.route.AboutNavigation
import com.example.composeime.route.SettingsNavigation
import splitties.systemservices.inputMethodManager

@Composable
fun Options(navController: NavController) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        val ctx = LocalContext.current
        Text(text = "Compose Keyboard")
        var text by remember { mutableStateOf("Try here") }
        Spacer(modifier = Modifier.height(16.dp))
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            ctx.startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
        }) {
            Text(text = "Enable IME")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            inputMethodManager.showInputMethodPicker()
        }) {
            Text(text = "Select IME")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = text,
            onValueChange = {
                text = it
            },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate(SettingsNavigation.Home.route) {
                    launchSingleTop = true
                }
            }) {
            Text(text = "preference")
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate(AboutNavigation.About.route) {
                    launchSingleTop = true
                }
            }) {
            Text(text = "about")
        }
    }
}