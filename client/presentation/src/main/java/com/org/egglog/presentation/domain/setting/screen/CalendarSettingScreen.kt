package com.org.egglog.presentation.domain.setting.screen

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.theme.ClientTheme
import com.org.egglog.presentation.utils.widthPercent

@Composable
fun CalendarSettingScreen() {
    val context = LocalContext.current
    Surface {
        Column(
            Modifier.fillMaxSize()
            .systemBarsPadding()
            .imePadding()
        ) {
            BasicHeader(
                title = "캘린더 설정",
                hasTitle = true,
                hasArrow = true,
                hasProgressBar = true,
                onClickBack = { (context as? Activity)?.onBackPressed() },
                onClickLink = { },
                onClickMenus = { },
                selectedOption = null
            )
            Column(
                Modifier
                    .padding(horizontal = 8.widthPercent(context).dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

            }
        }
    }
}

@Preview
@Composable
private fun CalendarSettingScreenPreview() {
    ClientTheme {
        CalendarSettingScreen()
    }
}