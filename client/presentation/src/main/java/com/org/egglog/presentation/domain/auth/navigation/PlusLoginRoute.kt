package com.org.egglog.presentation.domain.auth.navigation

sealed class PlusLoginRoute(val name: String) {
    data object AgreeScreen: PlusLoginRoute("AgreeScreen")
    data object AddInfoScreen: PlusLoginRoute("AddInfoScreen")
}