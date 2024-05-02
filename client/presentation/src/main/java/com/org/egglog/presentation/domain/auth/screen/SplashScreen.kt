package com.org.egglog.presentation.domain.auth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.R
import com.org.egglog.presentation.utils.widthPercent

@Composable
fun SplashScreen() {
    Surface {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(color = Warning300),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.weight(1f))
            LocalImageLoader(imageUrl = R.drawable.dark, Modifier.size(60.widthPercent(LocalContext.current).dp))
            Spacer(modifier = Modifier.weight(1f))
            Box {
                Text(text = "Copyright 2024. 호남향우회", color = White, modifier = Modifier.padding(bottom = 24.dp), style = Typography.labelLarge)
            }
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    ClientTheme {
        SplashScreen()
    }
}