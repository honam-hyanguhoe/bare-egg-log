package com.org.egglog.presentation.domain.myCalendar.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.org.egglog.presentation.domain.community.navigation.CommunityRoute
import com.org.egglog.presentation.domain.myCalendar.screen.ExcelListScreen
import com.org.egglog.presentation.domain.myCalendar.screen.MyCalendarScreen
import com.org.egglog.presentation.domain.setting.navigation.SettingRoute
import com.org.egglog.presentation.domain.setting.screen.CalendarAddScreen
import com.org.egglog.presentation.domain.setting.screen.CalendarSettingScreen
import com.org.egglog.presentation.domain.setting.screen.WorkSettingScreen

@Composable
fun MyCalendarNavigationHost() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MyCalendarRoute.MyCalendarScreen.name
    ) {
        composable(route = MyCalendarRoute.MyCalendarScreen.name) {
            MyCalendarScreen(
                onNavigateToExcelScreen = { currentYear, currentMonth ->
                    navController.navigate(
                        route = "${MyCalendarRoute.ExcelListScreen.name}/$currentYear/$currentMonth"
                    )
                },
                onNavigateToCalendarSettingScreen = {
                    navController.navigate(
                        SettingRoute.CalendarSettingScreen.name
                    )
                },
                onNavigateToWorkSettingScreen = {
                    navController.navigate(
                        SettingRoute.WorkSettingScreen.name
                    )
                }
            )
        }

        composable(
            route = "${MyCalendarRoute.ExcelListScreen.name}/{currentYear}/{currentMonth}",
            arguments = listOf(
                navArgument("currentYear") { type = NavType.IntType },
                navArgument("currentMonth") { type = NavType.IntType }
            )
        ) {backStackEntry ->
            val currentYear = backStackEntry.arguments!!.getInt("currentYear")
            val currentMonth = backStackEntry.arguments!!.getInt("currentMonth")
            ExcelListScreen(
                currentYear = currentYear,
                currentMonth = currentMonth,
                onNavigateToMyCalendarScreen = {
                    navController.navigate(
                        route = MyCalendarRoute.MyCalendarScreen.name,
                        builder = {
                            popUpTo(MyCalendarRoute.MyCalendarScreen.name) {
                                inclusive = true
                            }
                        }
                    )
                }
            )
        }

        composable(route = SettingRoute.CalendarSettingScreen.name) {
            CalendarSettingScreen(
                onNavigationToCalendarAddScreen = { navController.navigate(route = SettingRoute.CalendarAddScreen.name) }
            )
        }

        composable(route = SettingRoute.CalendarAddScreen.name) {
            CalendarAddScreen()
        }

        composable(route = SettingRoute.WorkSettingScreen.name) {
            WorkSettingScreen()
        }
    }
}