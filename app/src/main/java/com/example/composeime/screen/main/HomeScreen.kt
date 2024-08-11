package com.example.composeime.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.composeime.route.AboutNavigation
import com.example.composeime.route.SettingsNavigation
import com.example.composeime.screen.preference.component.menu.Menu
import com.example.composeime.screen.preference.component.menu.SettingsMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(isRoot = false, scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior())
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues = paddingValues)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {


            Options(navController = navController)
            Spacer(modifier = Modifier.weight(1f))

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    isRoot: Boolean,
    options: List<Menu> = listOf(),
    scrollBehavior: TopAppBarScrollBehavior
) = TopAppBar(
    title = {
        Text(text = "标题")
    },
    actions = {
        var expanded by remember { mutableStateOf(false) }

        IconButton(
            onClick = { expanded = true },
            enabled = isRoot
        ) {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = null
            )

            SettingsMenu(
                expanded = expanded,
                options = options,
                onClick = {
                    expanded = false
                }
            )
        }
    },
    scrollBehavior = scrollBehavior
)