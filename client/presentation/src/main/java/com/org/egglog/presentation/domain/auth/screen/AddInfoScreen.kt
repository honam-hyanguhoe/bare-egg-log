package com.org.egglog.presentation.domain.auth.screen

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.presentation.component.atoms.buttons.BigButton
import com.org.egglog.presentation.component.atoms.inputs.SearchInput
import com.org.egglog.presentation.component.atoms.inputs.SingleInput
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.domain.auth.activity.LoginActivity
import com.org.egglog.presentation.domain.auth.viewmodel.PlusLoginSideEffect
import com.org.egglog.presentation.domain.auth.viewmodel.PlusLoginViewModel
import com.org.egglog.presentation.domain.main.activity.MainActivity
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun AddInfoScreen(
    viewModel: PlusLoginViewModel = hiltViewModel(),
    onNavigateToAgreeScreen: () -> Unit
) {
    val state = viewModel.collectAsState().value
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

    AddInfoScreen(
        onNavigateToAgreeScreen = onNavigateToAgreeScreen,
        name = state.name,
        hospital = state.hospital,
        empNo = state.empNo,
        onNameChange = viewModel::onNameChange,
        onHospitalChange = viewModel::onHospitalChange,
        onEmpNoChange = viewModel::onEmpNoChange,
        onNavigateToMainActivity = viewModel::goMainActivity,
        onNavigateToLoginActivity = viewModel::goLoginActivity
    )
}

@Composable
fun AddInfoScreen(
    onNavigateToAgreeScreen: () -> Unit,
    name: String,
    hospital: String,
    empNo: String,
    onNameChange: (String) -> Unit,
    onHospitalChange: (String) -> Unit,
    onEmpNoChange: (String) -> Unit,
    onNavigateToMainActivity: () -> Unit,
    onNavigateToLoginActivity: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = NaturalWhite)
        ) {
            BasicHeader(
                title = "추가정보 입력",
                hasTitle = true,
                hasArrow = true,
                hasClose = true,
                hasProgressBar = true,
                onClickBack = onNavigateToAgreeScreen,
                onClickLink = { },
                onClickClose = onNavigateToLoginActivity,
                onClickMenus = { },
                selectedOption = null
            )

            Column(
                Modifier.padding(horizontal = 10.widthPercent(LocalContext.current).dp)
            ) {
                Spacer(modifier = Modifier.height(40.heightPercent(LocalContext.current).dp))

                Text(text = "이름을 입력해 주세요", style = Typography.headlineMedium)
                Text(text = "가입 시 입력한 이름을 입력해 주세요", style = Typography.displayMedium, color = Gray400)
                Spacer(modifier = Modifier.height(16.heightPercent(LocalContext.current).dp))
                SingleInput(
                    modifier = Modifier.fillMaxWidth(),
                    text = name,
                    onValueChange = onNameChange,
                    focusManager = focusManager,
                    placeholder = "이름 입력",
                )
                Spacer(modifier = Modifier.height(30.heightPercent(LocalContext.current).dp))

                Text(text = "병원(근무지) 입력", style = Typography.headlineMedium)
                Text(text = "현재 근무 하시는 병원(근무지)을 입력해 주세요", style = Typography.displayMedium, color = Gray400)
                Spacer(modifier = Modifier.height(16.heightPercent(LocalContext.current).dp))
                SearchInput(
                    modifier = Modifier.fillMaxWidth(),
                    text = hospital,
                    onValueChange = onHospitalChange,
                    focusManager = focusManager,
                    placeholder = "근무지 지역 선택",
                    onClickDone = { Log.d("click done: ", hospital) }
                )
                Spacer(modifier = Modifier.height(30.heightPercent(LocalContext.current).dp))

                Text(text = "사번 입력", style = Typography.headlineMedium)
                Text(text = "현재 근무 근무지의 사번은 입력해 주세요", style = Typography.displayMedium, color = Gray400)
                Spacer(modifier = Modifier.height(16.heightPercent(LocalContext.current).dp))
                SingleInput(
                    modifier = Modifier.fillMaxWidth(),
                    text = empNo,
                    onValueChange = onEmpNoChange,
                    focusManager = focusManager,
                    placeholder = "사번 입력",
                )
            }
            Spacer(modifier = Modifier.height(40.heightPercent(LocalContext.current).dp))
            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
                BigButton(
                    colors = ButtonColors(
                        containerColor = Warning300,
                        contentColor = NaturalWhite,
                        disabledContainerColor = Gray300,
                        disabledContentColor = NaturalWhite
                    ),
                    enabled = empNo.isNotEmpty() && hospital.isNotEmpty() && name.isNotEmpty(),
                    // TODO { 임시 처리 상태 }
                    onClick = onNavigateToMainActivity
                ) {
                    Text(text = "회원가입 완료하기", style = Typography.bodyMedium, color = NaturalWhite)
                }
            }
        }
    }
}

@Preview
@Composable
fun AddInfoScreenPreview() {
    ClientTheme {
        AddInfoScreen(
            onNavigateToAgreeScreen = { },
            name = "",
            hospital = "",
            empNo = "",
            onNameChange = { },
            onHospitalChange = { },
            onEmpNoChange = { },
            onNavigateToLoginActivity = { },
            onNavigateToMainActivity = { }
        )
    }
}