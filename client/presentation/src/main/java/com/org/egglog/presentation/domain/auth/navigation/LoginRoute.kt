package com.org.egglog.presentation.domain.auth.navigation

sealed class LoginRoute(val name: String) {
    data object LoginScreen: LoginRoute("LoginScreen")
    data object SignUpScreen: LoginRoute("SignUpScreen")
}