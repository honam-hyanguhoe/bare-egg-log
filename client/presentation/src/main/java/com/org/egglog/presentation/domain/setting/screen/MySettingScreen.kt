package com.org.egglog.presentation.domain.setting.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.org.egglog.presentation.theme.ClientTheme

@Composable
fun MySettingScreen() {
    Surface {
        Column {

        }
    }
}

@Preview
@Composable
private fun SettingScreenPreview() {
    ClientTheme {
        MySettingScreen()
    }
}