package com.org.egglog.presentation.domain.setting.navigation

import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.org.egglog.presentation.domain.setting.screen.AgreeDetailScreen
import com.org.egglog.presentation.domain.setting.screen.AskSettingScreen
import com.org.egglog.presentation.domain.setting.screen.BadgeScreen
import com.org.egglog.presentation.domain.setting.screen.CalendarAddScreen
import com.org.egglog.presentation.domain.setting.screen.CalendarSettingScreen
import com.org.egglog.presentation.domain.setting.screen.MySettingScreen
import com.org.egglog.presentation.domain.setting.screen.NotificationSettingScreen
import com.org.egglog.presentation.domain.setting.screen.PrivacyDetailScreen
import com.org.egglog.presentation.domain.setting.screen.SettingScreen
import com.org.egglog.presentation.domain.setting.screen.WorkSettingScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SettingNavigationHost() {
    val navController  = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SettingRoute.SettingScreen.name,
    ) {
        composable(route = SettingRoute.SettingScreen.name) {
            SettingScreen(
                onNavigateToCertificateBadeScreen = { navController.navigate(route = SettingRoute.CertificateBadgeScreen.name)},
                onNavigateToPrivacyDetailScreen = { navController.navigate(route = SettingRoute.PrivacyDetailScreen.name) },
                onNavigateToAgreeDetailScreen = { navController.navigate(route = SettingRoute.AgreeDetailScreen.name) },
                onNavigateToMySettingScreen = { navController.navigate(route = SettingRoute.MySettingScreen.name) },
                onNavigateToCalendarSettingScreen = { navController.navigate(route = SettingRoute.CalendarSettingScreen.name) },
                onNavigateToNotificationSettingScreen = { navController.navigate(route = SettingRoute.NotificationSettingScreen.name) },
                onNavigateToWorkSettingScreen = { navController.navigate(route = SettingRoute.WorkSettingScreen.name) },
                onNavigateToAskSettingScreen = { navController.navigate(route = SettingRoute.AskSettingScreen.name) },
            )
        }

        composable(route = SettingRoute.PrivacyDetailScreen.name) {
            PrivacyDetailScreen()
        }

        composable(route = SettingRoute.CalendarSettingScreen.name) {
            CalendarSettingScreen(
                onNavigationToCalendarAddScreen = { navController.navigate(route = SettingRoute.CalendarAddScreen.name) }
            )
        }

        composable(route = SettingRoute.AgreeDetailScreen.name) {
            AgreeDetailScreen()
        }

        composable(route = SettingRoute.MySettingScreen.name) {
            MySettingScreen()
        }

        composable(route = SettingRoute.CalendarAddScreen.name) {
            CalendarAddScreen()
        }

        composable(route = SettingRoute.WorkSettingScreen.name) {
            WorkSettingScreen()
        }

        composable(route = SettingRoute.AskSettingScreen.name) {
            AskSettingScreen()
        }

        composable(route = SettingRoute.NotificationSettingScreen.name) {
            NotificationSettingScreen()
        }

        composable(route = SettingRoute.CertificateBadgeScreen.name) {
            BadgeScreen(
                onNavigateToSettingScreen  = { navController.navigate(SettingRoute.SettingScreen.name)}
            )
        }
    }
}