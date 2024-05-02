package com.org.egglog.presentation.domain.auth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.theme.ClientTheme
import com.org.egglog.presentation.R
import com.org.egglog.presentation.component.atoms.buttons.AuthButton
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent

@Composable
fun LoginScreen() {
    Surface {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(color = NaturalWhite),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            LocalImageLoader(imageUrl = R.drawable.main_logo, modifier = Modifier.size(200.widthPercent(LocalContext.current).dp))
            Spacer(modifier = Modifier.height(150.heightPercent(LocalContext.current).dp))
            Column(
                modifier = Modifier
                    .background(color = NaturalWhite),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                Text(text = "SIGN WITH ___________________", color = Gray700, style = Typography.displayMedium)
                Spacer(modifier = Modifier.height(36.heightPercent(LocalContext.current).dp))
                Row{
                    AuthButton(type = "kakao", onClick = {})
                    Spacer(modifier = Modifier.width(16.widthPercent(LocalContext.current).dp))
                    AuthButton(type = "naver", onClick = {})
                    Spacer(modifier = Modifier.width(16.widthPercent(LocalContext.current).dp))
                    AuthButton(type = "google", onClick = {})
                }
            }
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