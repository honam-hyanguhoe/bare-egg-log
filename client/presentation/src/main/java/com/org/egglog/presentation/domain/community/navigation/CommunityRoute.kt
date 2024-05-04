package com.org.egglog.presentation.domain.community.navigation

sealed class CommunityRoute(val name : String){
    data object WritePostScreen : CommunityRoute("WritePostScreen")
}