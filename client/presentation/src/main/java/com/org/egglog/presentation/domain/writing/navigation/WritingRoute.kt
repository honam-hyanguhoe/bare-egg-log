package com.org.egglog.presentation.domain.writing.navigation

sealed class WritingRoute(val name : String){
    data object WritePostScreen : WritingRoute("WritePostScreen")
}