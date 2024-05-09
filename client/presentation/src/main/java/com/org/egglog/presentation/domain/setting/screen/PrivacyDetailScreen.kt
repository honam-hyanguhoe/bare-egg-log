package com.org.egglog.presentation.domain.setting.screen

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.org.egglog.presentation.component.organisms.webView.FullPageWebView
import com.org.egglog.presentation.theme.ClientTheme

@Composable
fun PrivacyDetailScreen() {
    val context = LocalContext.current
    Surface {
        Column(
            Modifier.fillMaxSize()
                .systemBarsPadding()
                .imePadding()
        ) {
            FullPageWebView(
                url = "https://www.egg-log.org/privacy",
                onClose = { (context as? Activity)?.onBackPressed() }
            )
        }
    }
}

@Preview
@Composable
private fun SettingScreenPreview() {
    ClientTheme {
        PrivacyDetailScreen()
    }
}