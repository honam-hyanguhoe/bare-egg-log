package com.org.egglog.presentation.domain.community.navigation

sealed class CommunityRoute(
    val name: String
){
    data object PostListScreen: CommunityRoute("PostListScreen")

    data object PostSearchScreen: CommunityRoute("PostSearchScreen")

    data object PostDetailScreen: CommunityRoute("PostDetailScreen")
}