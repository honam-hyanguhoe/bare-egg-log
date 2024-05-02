package com.org.egglog.presentation.domain.auth.screen

import android.content.Intent
import android.widget.Toast
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.theme.ClientTheme
import com.org.egglog.presentation.R
import com.org.egglog.presentation.component.atoms.buttons.AuthButton
import com.org.egglog.presentation.domain.auth.activity.MainActivity
import com.org.egglog.presentation.domain.auth.viewmodel.LoginSideEffect
import com.org.egglog.presentation.domain.auth.viewmodel.LoginViewModel
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginSideEffect.Toast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            LoginSideEffect.NavigateToMainActivity -> {
                context.startActivity(
                    Intent(
                        context, MainActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
        }
    }
    LoginScreen(viewModel::onAuthClick)
}

@Composable
fun LoginScreen(
    onAuthClick: (String) -> Unit
) {
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
                    AuthButton(type = "kakao", onClick = { onAuthClick("KAKAO") })
                    Spacer(modifier = Modifier.width(16.widthPercent(LocalContext.current).dp))
                    AuthButton(type = "naver", onClick = { onAuthClick("NAVER") })
                    Spacer(modifier = Modifier.width(16.widthPercent(LocalContext.current).dp))
                    AuthButton(type = "google", onClick = { onAuthClick("GOOGLE") })
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