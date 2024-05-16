package com.org.egglog.presentation.domain.main.navigation

sealed class MainRoute(val name: String) {
    data object MainScreen: MainRoute("MainScreen")

}