package com.org.egglog.presentation.domain.auth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.org.egglog.presentation.theme.ClientTheme
import com.org.egglog.presentation.theme.Warning300

@Composable
fun LoginScreen() {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Warning300)
        ) {
            Text(text = "login")
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    ClientTheme {
        LoginScreen()
    }
}