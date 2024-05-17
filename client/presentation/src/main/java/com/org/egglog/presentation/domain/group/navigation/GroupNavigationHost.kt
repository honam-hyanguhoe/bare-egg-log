package com.org.egglog.presentation.domain.group.navigation

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.org.egglog.presentation.domain.group.activity.GroupActivity
import com.org.egglog.presentation.domain.group.screen.GroupListScreen
import com.org.egglog.presentation.domain.group.screen.GroupDetailScreen
import com.org.egglog.presentation.domain.group.screen.InvitationScreen
import com.org.egglog.presentation.domain.group.screen.MemberManageScreen

@Composable
fun GroupNavigationHost(
    deepLinkUri: Uri?,
    groupName: String?,
    code: String?,
    groupId : Long?
) {
    val navController = rememberNavController()
    val context = LocalContext.current

    LaunchedEffect(deepLinkUri) {
        deepLinkUri?.let {
            Log.d("Invite Link", "gnh $deepLinkUri")
            val pathSegments = it.pathSegments

            if(pathSegments[0] == "invite"){
                if (code != null && groupName != null) {
                    navController.navigate(it.toString())
                } else {
                    navController.navigate(GroupRoute.GroupListScreen.name)
                }
            }else if(pathSegments[0] == "groups"){
                if (pathSegments.size > 1) {
                    val groupId = pathSegments[1]
                    Log.d("deep", "groupId $groupId")
                    navController.navigate(
                        route = "${GroupRoute.GroupDetailScreen.name}/$groupId"
                    )
                } else {
                    Log.d("deep", "groupList로 이동")
                    navController.navigate(GroupRoute.GroupListScreen.name)
                }

            }
        }
    }

    val baseUri = "egglog://honam.com/"
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
            route = "${baseUri}${GroupRoute.InviteScreen.name}",
            arguments = listOf(
                navArgument("code") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            ),
            deepLinks = listOf(navDeepLink {
                uriPattern = "${baseUri}${GroupRoute.InviteScreen.name}"
            })
        ) { backStackEntry ->
            val code = backStackEntry.arguments?.getString("code")
            val groupName = backStackEntry.arguments?.getString("name")
            InvitationScreen(
                code = code ?: "",
                groupName = groupName ?: "",
                onNavigateToGroupListScreen = {
                    navController.navigate(
                        route = GroupRoute.GroupListScreen.name
                    ) {
                        popUpTo(GroupRoute.GroupListScreen.name) {
                            inclusive = true
                        }
                    }
                })
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
                onNavigateToMemberManageScreen = { groupId ->
                    navController.navigate(
                        route = "${GroupRoute.MemberManageScreen.name}/$groupId"
                    )
                }
            )
        }

        composable(
            route = "${GroupRoute.MemberManageScreen.name}/{groupId}",
            arguments = listOf(navArgument("groupId") { type = NavType.LongType }),

            ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getLong("groupId") ?: 0
            MemberManageScreen(
                groupId = groupId,
                onNavigateToGroupDetailScreen = {
                    navController.navigate(
                        route = "${GroupRoute.GroupDetailScreen.name}/$groupId"
                    ) {
                        popUpTo(route = "${GroupRoute.GroupDetailScreen.name}/$groupId") {
                            inclusive = true
                        }
                    }
                },

                )
        }
    }
}