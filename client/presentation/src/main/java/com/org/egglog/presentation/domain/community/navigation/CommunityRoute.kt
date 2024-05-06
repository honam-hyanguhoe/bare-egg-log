package com.org.egglog.presentation.domain.community.navigation

sealed class CommunityRoute(
    val name: String
){
    object PostListScreen: CommunityRoute("PostListScreen")

    object SearchListScreen: CommunityRoute("SearchListScreen")

    object PostDetailScreen: CommunityRoute("PostDetailScreen")
}