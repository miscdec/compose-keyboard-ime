package com.example.composeime.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.composeime.screen.about.AboutScreen

enum class AboutNavigation(val route: String) {
    About("About")
}

fun NavGraphBuilder.aboutScreen(
    navController: NavController
) = navigation(
    startDestination = AboutNavigation.About.route,
    route = MainDestinations.About.route
) {
    composable(
        route = AboutNavigation.About.route
    ) {
        AboutScreen(
            navController = navController
        )
    }
}
