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
import com.org.egglog.presentation.domain.community.screen.PostSearchScreen

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
                    navController.navigate(
                        route = "${CommunityRoute.PostDetailScreen.name}/$postId"
                    )
                },
                onNavigateToSearchScreen = { hospitalId, groupId ->
                    navController.navigate(
                        route = "${CommunityRoute.PostSearchScreen.name}/$hospitalId/$groupId"
                    )
                }
            )
        }

        composable(
            route = "${CommunityRoute.PostSearchScreen.name}/{hospitalId}/{groupId}",
            arguments = listOf(navArgument("hospitalId") {type = NavType.IntType}, navArgument("groupId") {type = NavType.IntType})
        ) {
            backStackEntry ->
            val hospitalId = backStackEntry.arguments?.getInt("hospitalId")
            val groupId = backStackEntry.arguments?.getInt("groupId")
            PostSearchScreen(
                hospitalId = hospitalId?: -1,
                groupId = groupId?: -1,
                onNavigateToPostListScreen = {
                    navController.navigate(
                        route = CommunityRoute.PostListScreen.name,
                        builder = {
                            popUpTo(CommunityRoute.PostListScreen.name) {
                                inclusive = true
                            }
                        }
                    )
                },
                onNavigateToDetailScreen = { postId ->
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
                        route = CommunityRoute.PostListScreen.name,
                        builder = {
                            popUpTo(CommunityRoute.PostListScreen.name) {
                            inclusive = true // 목적지도 포함하여 스택을 비웁니다.
                            }
                        }
                    )
                }
            )
        }
    }
}