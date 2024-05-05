package com.org.egglog.presentation.domain.community.posteditor.navigation

sealed class PostEditorRoute(val name : String){
    data object WritePostScreen : PostEditorRoute("WritePostScreen")
}