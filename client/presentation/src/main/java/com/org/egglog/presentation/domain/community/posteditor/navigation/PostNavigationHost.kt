package com.org.egglog.presentation.domain.community.posteditor.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.org.egglog.presentation.domain.community.posteditor.screen.WritePostScreen
import com.org.egglog.presentation.domain.community.posteditor.viewmodel.WritePostViewModel

@Composable
fun PostNavigationHost(
    onFinish : () -> Unit
) {
    val navController = rememberNavController()
    val sharedViewModel : WritePostViewModel = viewModel()

    NavHost(
        navController = navController, startDestination = PostEditorRoute.WritePostScreen.name
    ) {
        composable(route = PostEditorRoute.WritePostScreen.name){
            WritePostScreen(
                viewModel = sharedViewModel,
                onCloseClick = onFinish
            )
        }
    }
}