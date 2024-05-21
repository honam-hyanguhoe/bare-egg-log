package com.org.egglog.presentation.domain.main.navigation

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.org.egglog.presentation.domain.auth.navigation.PlusLoginRoute
import com.org.egglog.presentation.domain.auth.screen.AddInfoScreen
import com.org.egglog.presentation.domain.auth.screen.AgreeScreen
import com.org.egglog.presentation.domain.auth.screen.TestScreen
import com.org.egglog.presentation.domain.community.activity.CommunityActivity
import com.org.egglog.presentation.domain.group.activity.GroupActivity
import com.org.egglog.presentation.domain.main.screen.MainScreen
import com.org.egglog.presentation.domain.myCalendar.activity.MyCalendarActivity

@Composable
fun MainNavigationHost(deepLinkUri: Uri?) {
    val navController = rememberNavController()
    val context = LocalContext.current
    LaunchedEffect(deepLinkUri) {
        deepLinkUri?.let {
            val pathSegments = it.pathSegments
            if (pathSegments[0] == "invite") {
                val code = pathSegments[1]
                val name = pathSegments[2]
                Log.d("deep", "nvh $code $name")
                if (code != null) {
                    val intent = Intent(context, GroupActivity::class.java).apply {
                        data = it
                        putExtra("code", code)
                        putExtra("name", name)
                    }
                    context.startActivity(intent)
                }
            } else if (
                pathSegments[0] == "groups"
            ) {
                Log.d("deep", "pathSegments $pathSegments")
                if (pathSegments.size > 1) {
                    val groupId = pathSegments[1]
                    Log.d("deep", "groupId $groupId")
                    val intent = Intent(context, GroupActivity::class.java).apply {
                        data = it
                        putExtra("groupId", groupId)
                    }
                    context.startActivity(intent)
                } else {
                    Log.d("deep", "groupList로 이동")
                    val intent = Intent(context, GroupActivity::class.java).apply {
                        data = it
                    }
                    context.startActivity(intent)
                }
            } else if (pathSegments[0] == "community") {
                val intent = Intent(context, CommunityActivity::class.java).apply {
                    data = it
                }
                context.startActivity(intent)
            } else if (pathSegments[0] == "main") {
                navController.navigate(MainRoute.MainScreen.name)
            }else if (pathSegments[0] == "mycalendar") {
                val intent = Intent(context, MyCalendarActivity::class.java).apply {
                    data = it
                }
                context.startActivity(intent)
            }
            else{
                navController.navigate(MainRoute.MainScreen.name)
            }
        }
    }

    NavHost(navController = navController, startDestination = MainRoute.MainScreen.name) {
        composable(
            route = MainRoute.MainScreen.name,
        ) {
            MainScreen()
        }

//        composable(
//            route = MainRoute.InviteScreen.name,
//            deepLinks = listOf(navDeepLink { uriPattern = MainRoute.InviteScreen.name })
//        ) {
//            MainScreen()
//        }
    }
}