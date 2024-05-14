package com.org.egglog.presentation.domain.group.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.org.egglog.presentation.domain.group.screen.GroupListScreen
import com.org.egglog.presentation.domain.group.screen.GroupDetailScreen
import com.org.egglog.presentation.domain.group.screen.MemberManageScreen

@Composable
fun GroupNavigationHost(

) {
    val navController = rememberNavController()

    NavHost(
        navController = navController, startDestination = GroupRoute.GroupListScreen.name
    ) {
        composable(route = GroupRoute.GroupListScreen.name) {
            GroupListScreen(
                onNavigateToGroupDetailScreen = { groupId ->
                    navController.navigate(
                        route = "${GroupRoute.GroupDetailScreen.name}/$groupId"
                    )
                }
            )
        }

        composable(
            route = "${GroupRoute.GroupDetailScreen.name}/{groupId}",
            arguments = listOf(navArgument("groupId") { type = NavType.LongType })
        ) { backStackEntry ->
            GroupDetailScreen(
                groupId = backStackEntry.arguments?.getLong("groupId") ?: 0,
                onNavigateToGroupListScreen = {
                    navController.navigate(
                        route = GroupRoute.GroupListScreen.name
                    ) {
                        popUpTo(GroupRoute.GroupListScreen.name) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToMemberManageScreen = {
                    groupId -> navController.navigate(
                        route = "${GroupRoute.MemberManageScreen.name}/$groupId"
                    )
                }
            )
        }

        composable(
            route = "${GroupRoute.MemberManageScreen.name}/{groupId}",
            arguments = listOf(navArgument("groupId"){type = NavType.LongType})
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getLong("groupId") ?: 0
            MemberManageScreen(
                groupId = groupId,
                onNavigateToGroupDetailScreen = {
                    navController.navigate(
                        route = "${GroupRoute.GroupDetailScreen.name}/$groupId"
                    ){
                        popUpTo(route = "${GroupRoute.GroupDetailScreen.name}/$groupId"){
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}