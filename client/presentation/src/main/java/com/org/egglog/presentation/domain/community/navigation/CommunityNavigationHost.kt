package com.org.egglog.presentation.domain.community.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.org.egglog.presentation.domain.community.screen.PostDetailScreen
import com.org.egglog.presentation.domain.community.screen.PostListScreen

@Composable
fun CommunityNavigationHost() {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = CommunityRoute.PostListScreen.name
    ) {
        composable(route = CommunityRoute.PostListScreen.name) {
            PostListScreen(
                onNavigateToDetailScreen = { postId ->
                    Log.e("NavController", "${postId}")
                    navController.navigate(
                        route = "${CommunityRoute.PostDetailScreen.name}/$postId" // postId를 목적지에 전달
                    )
                }
            )
        }

        composable(
            route = "${CommunityRoute.PostDetailScreen.name}/{postId}",
            arguments = listOf(navArgument("postId") {type = NavType.IntType})
            ) {
            backStackEntry ->
             val postId = backStackEntry.arguments?.getInt("postId")
            PostDetailScreen(
                postId = postId!!,
                onNavigateToPostListScreen = {
                    navController.navigate(
                        route = "${CommunityRoute.PostListScreen}"
                    )
                }
            )
        }
    }
}