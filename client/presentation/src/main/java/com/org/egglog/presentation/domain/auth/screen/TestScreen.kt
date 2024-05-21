package com.org.egglog.presentation.domain.auth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.org.egglog.presentation.component.organisms.dialogs.WebViewDialog
import com.org.egglog.presentation.theme.NaturalWhite

@Composable
fun TestScreen(){
    val openDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Surface {
        Column(
            modifier = Modifier.fillMaxSize().background(color = NaturalWhite)
        ) {
            Text("테스트 스크린임")
            Button(onClick = { openDialog.value = true }) {
                Text("Show Dialog")
            }

            when {
                openDialog.value -> {
                    WebViewDialog(url = "https://www.egg-log.org/privacy", onDismiss = { openDialog.value = false  }, context = context)
                }
            }
        }
    }
}

@Preview
@Composable
fun TestPreviewScreen(){
    TestScreen()
}