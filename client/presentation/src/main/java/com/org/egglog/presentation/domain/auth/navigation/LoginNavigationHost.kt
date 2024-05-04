package com.org.egglog.presentation.domain.auth.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.org.egglog.presentation.domain.auth.screen.LoginScreen
import com.org.egglog.presentation.domain.community.navigation.CommunityRoute
import com.org.egglog.presentation.domain.community.screen.WritePostScreen
import com.org.egglog.presentation.domain.community.viewmodel.WritePostViewModel

@Composable
fun LoginNavigationHost() {
    val navController  = rememberNavController()
    val sharedViewModel : WritePostViewModel = viewModel()

    NavHost(
        navController = navController,
//        startDestination = LoginRoute.LoginScreen.name,
        startDestination = CommunityRoute.WritePostScreen.name
    ) {
        composable(route = CommunityRoute.WritePostScreen.name){
            WritePostScreen(
                viewModel = sharedViewModel,
                onCloseClick = { Log.d("커뮤니티", "글 작성 닫기 버튼 클릭")}
            )
        }
        composable(route = LoginRoute.LoginScreen.name) {
            LoginScreen()
        }
//        composable(route = AuthRoute.SignUpScreen.name) {
//            SignUpScreen(
//                id = "",
//                username = "",
//                password1 = "",
//                password2 = "",
//                onIdChange = {},
//                onUsernameChange = {},
//                onPassword1Change = {},
//                onPassword2Change = {},
//                onSignUpClick = {}
//            )
//        }
    }
}