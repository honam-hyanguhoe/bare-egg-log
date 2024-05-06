package com.org.egglog.presentation.domain.community.navigation

sealed class CommunityRoute(
    val name: String
){
    data object PostListScreen: CommunityRoute("PostListScreen")

    data object SearchListScreen: CommunityRoute("SearchListScreen")

    data object PostDetailScreen: CommunityRoute("PostDetailScreen")
}