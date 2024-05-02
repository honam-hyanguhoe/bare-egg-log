package com.org.egglog.presentation.domain.auth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.org.egglog.presentation.domain.auth.screen.LoginScreen

@Composable
fun LoginNavigationHost() {
    val navController  = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute.LoginScreen.name,
    ) {
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