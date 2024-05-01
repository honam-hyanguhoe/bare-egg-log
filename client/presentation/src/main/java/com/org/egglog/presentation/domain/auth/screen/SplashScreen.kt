package com.org.egglog.presentation.domain.auth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.org.egglog.presentation.theme.*

@Composable
fun SplashScreen() {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Warning300)
        )
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    ClientTheme {
        SplashScreen()
    }
}