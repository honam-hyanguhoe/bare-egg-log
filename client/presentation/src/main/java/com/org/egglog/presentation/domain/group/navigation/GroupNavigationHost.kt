package com.org.egglog.presentation.domain.group.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.org.egglog.presentation.domain.group.screen.GroupListScreen
import com.org.egglog.presentation.domain.group.viewmodel.GroupListViewModel

@Composable
fun GroupNavigationHost(

) {
    val navController = rememberNavController()
    val sharedViewModel : GroupListViewModel = viewModel()

    NavHost(
        navController = navController, startDestination = GroupRoute.GroupListScreen.name
    ) {
        composable(route = GroupRoute.GroupListScreen.name){
            GroupListScreen(

            )
        }
    }
}