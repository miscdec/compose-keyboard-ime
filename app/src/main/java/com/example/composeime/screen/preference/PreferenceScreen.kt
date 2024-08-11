package com.example.composeime.screen.preference

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.composeime.BuildConfig
import com.example.composeime.R
import com.example.composeime.route.AboutNavigation
import com.example.composeime.route.SettingsNavigation
import com.example.composeime.screen.preference.component.SettingNormalItem
import com.sanmer.mrepo.ui.utils.navigatePopUpTo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController
) {

    val context = LocalContext.current

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    BackHandler {
        navController.popBackStack()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(title = { /*TODO*/ })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            SettingNormalItem(
                imageVector = Icons.Outlined.Palette,
                text = stringResource(id = R.string.settings_style),
                subText = stringResource(id = R.string.settings_style_desc),
                onClick = {
                    navController.navigate(SettingsNavigation.Style.route)
                }
            )

            SettingNormalItem(
                imageVector = Icons.Outlined.Info,
                text = stringResource(id = R.string.settings_about),
                subText = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                onClick = {
                    navController.navigate(AboutNavigation.About.route)
                }
            )

        }
    }
}

