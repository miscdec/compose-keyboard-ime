package com.example.composeime.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.composeime.screen.about.AboutScreen
import com.example.composeime.screen.preference.SettingsScreen
import com.example.composeime.screen.preference.style.StyleSettingsScreen

enum class SettingsNavigation(val route: String) {
    Home("Settings"),
    Style("style"),
    Repositories("Repositories"),
    App("App"),
    WorkingMode("WorkingMode"),

}

private val subScreens = listOf(
    SettingsNavigation.Style.route,
)

fun NavGraphBuilder.settingsScreen(
    navController: NavController
) = navigation(
    startDestination = SettingsNavigation.Home.route,
    route = MainDestinations.Settings.route
) {
    composable(
        route = SettingsNavigation.Home.route
    ) {
        SettingsScreen(
            navController = navController
        )
    }

    composable(
        route = SettingsNavigation.Style.route
    ) {
        StyleSettingsScreen(
            navController = navController
        )
    }

}