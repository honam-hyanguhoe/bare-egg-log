package com.org.egglog.presentation.domain.writing.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.org.egglog.presentation.domain.writing.screen.WritePostScreen
import com.org.egglog.presentation.domain.writing.viewmodel.WritePostViewModel

@Composable
fun WritingNavigationHost(
    onFinish : () -> Unit
) {
    val navController = rememberNavController()
    val sharedViewModel : WritePostViewModel = viewModel()

    NavHost(
        navController = navController, startDestination = WritingRoute.WritePostScreen.name
    ) {
        composable(route = WritingRoute.WritePostScreen.name){
            WritePostScreen(
                viewModel = sharedViewModel,
                onCloseClick = onFinish
            )
        }
    }
}