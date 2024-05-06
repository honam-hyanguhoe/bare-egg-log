package com.org.egglog.presentation.domain.community.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.org.egglog.presentation.domain.community.screen.PostListScreen

@Composable
fun CommunityNavigationHost() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = CommunityRoute.PostListScreen.name
    ) {
        composable(route = CommunityRoute.PostListScreen.name) {
            PostListScreen()
        }
    }
}