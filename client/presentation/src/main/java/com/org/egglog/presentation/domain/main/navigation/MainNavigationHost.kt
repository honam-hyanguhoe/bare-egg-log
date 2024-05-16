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
import com.org.egglog.presentation.domain.group.activity.GroupActivity
import com.org.egglog.presentation.domain.main.screen.MainScreen

@Composable
fun MainNavigationHost(deepLinkUri: Uri?) {
    val navController = rememberNavController()
    val context = LocalContext.current
    LaunchedEffect(deepLinkUri) {
        deepLinkUri?.let {
            val pathSegments = it.pathSegments
            if (pathSegments[0] == "invite"){
                val code = pathSegments[1]
                val name = pathSegments[2]

                if (code != null) {
                    // 초대 승인 페이지로 이동
                    val intent = Intent(context, GroupActivity::class.java).apply {
                        data = it
                        putExtra("code", code)
                        putExtra("name", name)
                    }
                    context.startActivity(intent)
                }
            }
//            val destination = it.get
//            val code = param?.get(0) ?: ""
//            val name = param?.get(1) ?: ""
//            if(code != null){
//                // 초대 승인 페이지로 이동
//                val intent = Intent(context, GroupActivity::class.java).apply {
//                    data = it
//                    putExtra("code", code)
//                    putExtra("name", name)
//                }
//                context.startActivity(intent)
//            }
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