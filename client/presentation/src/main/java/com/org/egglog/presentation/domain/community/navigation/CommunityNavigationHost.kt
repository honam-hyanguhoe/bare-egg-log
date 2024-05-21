package com.org.egglog.presentation.domain.community.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.org.egglog.presentation.domain.community.screen.ModifyPostScreen
import com.org.egglog.presentation.domain.community.screen.PostDetailScreen
import com.org.egglog.presentation.domain.community.screen.PostListScreen
import com.org.egglog.presentation.domain.community.screen.PostSearchScreen
import com.org.egglog.presentation.domain.community.screen.WritePostScreen
import com.org.egglog.presentation.domain.group.navigation.GroupRoute

@Composable
fun CommunityNavigationHost(
    deepLinkUri: Uri?,
) {

    val navController = rememberNavController()
    val context = LocalContext.current

    LaunchedEffect(deepLinkUri) {
        deepLinkUri?.let {
            Log.d("Invite Link", "gnh $deepLinkUri")
            val pathSegments = it.pathSegments

            if(pathSegments[0] == "community"){
                if (pathSegments.size > 1) {
                    val postId = pathSegments[1]
                    Log.d("deep", "postId $postId")
                    navController.navigate(
                        route = "${CommunityRoute.PostDetailScreen.name}/$postId"
                    )
                } else {
                    Log.d("deep", "전체 리스트로 이동")
                    navController.navigate(CommunityRoute.PostListScreen.name)
                }
            }else {
                navController.navigate(CommunityRoute.PostListScreen.name)
            }
        }
    }


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
                },
                onNavigateToWriteScreen = { hospitalId, groupId ->
                    navController.navigate(
                        route = "${CommunityRoute.WritePostScreen.name}/$hospitalId/$groupId"
                    )
                }
            )
        }

        composable(
            route = "${CommunityRoute.PostSearchScreen.name}/{hospitalId}/{groupId}",
            arguments = listOf(
                navArgument("hospitalId") { type = NavType.IntType },
                navArgument("groupId") { type = NavType.IntType })
        ) { backStackEntry ->
            val hospitalId = backStackEntry.arguments?.getInt("hospitalId")
            val groupId = backStackEntry.arguments?.getInt("groupId")
            PostSearchScreen(
                hospitalId = hospitalId ?: -1,
                groupId = groupId ?: -1,
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
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId")
            PostDetailScreen(
                postId = postId!!,
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
                onNavigateToModifyScreen = { postId ->
                    navController.navigate(
                        route = "${CommunityRoute.ModifyPostScreen.name}/$postId" // postId를 목적지에 전달
                    )
                }
            )
        }


        composable(
            route = "${CommunityRoute.WritePostScreen.name}/{hospitalId}/{groupId}",
            arguments = listOf(
                navArgument("hospitalId") { type = NavType.IntType },
                navArgument("groupId") { type = NavType.IntType })
        ) { backStackEntry ->
            val hospitalId = backStackEntry.arguments?.getInt("hospitalId")
            val groupId = backStackEntry.arguments?.getInt("groupId")
            WritePostScreen(
                hospitalId = hospitalId ?: -1,
                groupId = groupId ?: -1,
                onNavigateToListScreen = {
                    navController.navigate(
                        route = CommunityRoute.PostListScreen.name,
                        builder = {
                            popUpTo(CommunityRoute.PostListScreen.name) {
                                inclusive = true
                            }
                        }
                    )
                }
            )
        }

        composable(
            route = "${CommunityRoute.ModifyPostScreen.name}/{postId}",
            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) { backStackEntry ->
            val postId = backStackEntry.arguments?.getInt("postId")
            ModifyPostScreen(
                postId = postId!!,
                onNavigateToDetailScreen = { postId ->
                    navController.navigate(
                        route = "${CommunityRoute.PostDetailScreen.name}/$postId",
                        builder = {
                            popUpTo(CommunityRoute.PostDetailScreen.name) {
                                inclusive = true
                            }
                        }
                    )
                }
            )

        }
    }
}