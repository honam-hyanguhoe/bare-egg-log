package com.org.egglog.presentation.domain.setting.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.org.egglog.presentation.component.organisms.webView.FullPageWebView
import com.org.egglog.presentation.theme.ClientTheme

@Composable
fun AgreeDetailScreen(
    onNavigateToSettingScreen: () -> Unit
) {
    Surface {
        Column(Modifier.fillMaxSize()) {
            FullPageWebView(
                url = "https://www.egg-log.org/agreement",
                onClose = { onNavigateToSettingScreen() }
            )
        }
    }
}

@Preview
@Composable
private fun SettingScreenPreview() {
    ClientTheme {
        AgreeDetailScreen({})
    }
}