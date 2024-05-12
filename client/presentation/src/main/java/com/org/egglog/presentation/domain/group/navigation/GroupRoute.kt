package com.org.egglog.presentation.domain.group.navigation

sealed class GroupRoute(val name : String){
    data object GroupListScreen : GroupRoute("GroupListScreen")
    data object GroupDetailScreen : GroupRoute("GroupDetailScreen")
}