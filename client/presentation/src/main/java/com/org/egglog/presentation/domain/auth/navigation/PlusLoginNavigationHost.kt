package com.org.egglog.presentation.domain.auth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.org.egglog.presentation.domain.auth.screen.AddInfoScreen
import com.org.egglog.presentation.domain.auth.screen.AgreeScreen

@Composable
fun PlusLoginNavigationHost() {
    val navController  = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PlusLoginRoute.AgreeScreen.name,
    ) {
        composable(route = PlusLoginRoute.AgreeScreen.name) {
            AgreeScreen(
                onNavigateToAddInfoScreen = { navController.navigate(route = PlusLoginRoute.AddInfoScreen.name) }
            )
        }
        composable(route = PlusLoginRoute.AddInfoScreen.name) {
            AddInfoScreen(
                onNavigateToAgreeScreen = { navController.navigate(route = PlusLoginRoute.AgreeScreen.name) }
            )
        }
    }
}