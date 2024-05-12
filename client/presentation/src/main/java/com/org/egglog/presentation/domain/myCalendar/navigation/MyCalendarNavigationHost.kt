package com.org.egglog.presentation.domain.myCalendar.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.org.egglog.presentation.domain.community.navigation.CommunityRoute
import com.org.egglog.presentation.domain.myCalendar.screen.ExcelListScreen
import com.org.egglog.presentation.domain.myCalendar.screen.MyCalendarScreen

@Composable
fun MyCalendarNavigationHost() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = MyCalendarRoute.MyCalendarScreen.name
    ) {
        composable(route = MyCalendarRoute.MyCalendarScreen.name) {
            MyCalendarScreen(
                onNavigateToExcelScreen = {
                    navController.navigate(
                        MyCalendarRoute.ExcelListScreen.name
                    )
                }
            )
        }

        composable(route = MyCalendarRoute.ExcelListScreen.name) {
            ExcelListScreen(
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
    }
}