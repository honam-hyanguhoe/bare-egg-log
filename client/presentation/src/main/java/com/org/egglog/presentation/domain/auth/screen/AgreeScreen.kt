package com.org.egglog.presentation.domain.auth.screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.presentation.component.atoms.buttons.BigButton
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.component.organisms.agreeList.AgreeList
import com.org.egglog.presentation.domain.auth.activity.LoginActivity
import com.org.egglog.presentation.domain.auth.viewmodel.PlusLoginSideEffect
import com.org.egglog.presentation.domain.auth.viewmodel.PlusLoginViewModel
import com.org.egglog.presentation.domain.main.activity.MainActivity
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun AgreeScreen(
    viewModel: PlusLoginViewModel = hiltViewModel(),
    onNavigateToAddInfoScreen: () -> Unit
) {
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is PlusLoginSideEffect.Toast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            PlusLoginSideEffect.NavigateToMainActivity -> {
                context.startActivity(
                    Intent(
                        context, MainActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
            PlusLoginSideEffect.NavigateToLoginActivity -> {
                context.startActivity(
                    Intent(
                        context, LoginActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
        }
    }

    AgreeScreen(onNavigateToAddInfoScreen, viewModel::goLoginActivity)
}

@Composable
fun AgreeScreen(
    onNavigateToAddInfoScreen: () -> Unit,
    onNavigateToLoginActivity: () -> Unit
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = NaturalWhite)
        ) {
            BasicHeader(
                title = "약관동의",
                hasTitle = true,
                hasArrow = true,
                hasClose = true,
                hasProgressBar = true,
                onClickBack = onNavigateToLoginActivity,
                onClickLink = { },
                onClickClose = onNavigateToLoginActivity,
                onClickMenus = { },
                selectedOption = null
            )
            Column(
                Modifier.padding(horizontal = 6.widthPercent(LocalContext.current).dp)
            ) {
                Spacer(modifier = Modifier.height(80.heightPercent(LocalContext.current).dp))
                Text(text = "이용 약관에 대한 동의가 필요해요", style = Typography.headlineMedium)
                Spacer(modifier = Modifier.height(20.heightPercent(LocalContext.current).dp))
                val (ageChecked, setAgeClick) = remember { mutableStateOf(false) }
                val (agreeChecked, setAgreeClick) = remember { mutableStateOf(false) }
                val (infoChecked, setInfoClick) = remember { mutableStateOf(false) }
                AgreeList(ageChecked, setAgeClick, agreeChecked, setAgreeClick, infoChecked, setInfoClick)
                Spacer(modifier = Modifier.height(80.heightPercent(LocalContext.current).dp))
                Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
                    BigButton(
                        colors = ButtonColors(containerColor = Warning300, contentColor = NaturalWhite, disabledContainerColor = Gray300, disabledContentColor = NaturalWhite),
                        onClick = { onNavigateToAddInfoScreen() },
                        enabled = agreeChecked && ageChecked && infoChecked
                    ) {
                        Text(text = "다음", style = Typography.bodyLarge, color = NaturalWhite)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AgreeScreenPreview() {
    ClientTheme {
        AgreeScreen({}, {})
    }
}