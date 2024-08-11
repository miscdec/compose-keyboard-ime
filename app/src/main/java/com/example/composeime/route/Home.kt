package com.example.composeime.route

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.composeime.screen.main.HomeScreen

enum class HomeNavigation(val route: String) {
    Home("Home"),
}

fun NavGraphBuilder.homeScreen(
    navController: NavController
) = navigation(
    startDestination = HomeNavigation.Home.route,
    route = MainDestinations.Home.route
) {
    composable(
        route = HomeNavigation.Home.route
    ) {
        HomeScreen(
            navController = navController
        )
    }
}
