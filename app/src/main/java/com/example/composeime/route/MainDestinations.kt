package com.example.composeime.route

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.example.composeime.R
import com.sanmer.mrepo.ui.utils.navigatePopUpTo

enum class MainDestinations(
    val route: String,
    @StringRes val label: Int? = null,
    val icon: ImageVector,
) {

    Home(
        route = "HomeScreen",
        label = R.string.screen_home,
        icon = Icons.Outlined.Home
    ),
    Settings(
        route = "SettingsScreen",
        label = R.string.screen_settings,
        icon = Icons.Outlined.Settings,
    ),
    About(
        route = "AboutScreen",
        icon = Icons.Outlined.Info
    )
}


//fun NavController.navigateToSettings() = navigatePopUpTo(MainDestinations.Settings.route)