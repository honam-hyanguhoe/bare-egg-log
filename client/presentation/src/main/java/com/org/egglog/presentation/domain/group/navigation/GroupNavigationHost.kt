package com.org.egglog.presentation.domain.group.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.org.egglog.presentation.domain.community.posteditor.navigation.PostEditorRoute
import com.org.egglog.presentation.domain.community.posteditor.screen.WritePostScreen
import com.org.egglog.presentation.domain.community.posteditor.viewmodel.WritePostViewModel
import com.org.egglog.presentation.domain.group.screen.GroupListScreen

@Composable
fun GroupNavigationHost(

) {
    val navController = rememberNavController()
    val sharedViewModel : WritePostViewModel = viewModel()

    NavHost(
        navController = navController, startDestination = GroupRoute.GroupListScreen.name
    ) {
        composable(route = GroupRoute.GroupListScreen.name){
            GroupListScreen(

            )
        }
    }
}