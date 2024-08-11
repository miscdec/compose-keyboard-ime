package com.example.composeime.route

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost() {
    val navHostController = rememberNavController()
    NavHost(
        navController = navHostController,
        startDestination = MainDestinations.Home.route
    ) {
        homeScreen(navController = navHostController)
        settingsScreen(navController = navHostController)
        aboutScreen(navController = navHostController)

    }
}



