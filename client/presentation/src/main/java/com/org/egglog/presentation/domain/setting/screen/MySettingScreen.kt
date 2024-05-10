package com.org.egglog.presentation.domain.setting.screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.model.UserHospital
import com.org.egglog.presentation.component.atoms.buttons.BigButton
import com.org.egglog.presentation.component.atoms.cards.ProfileCard
import com.org.egglog.presentation.component.atoms.dialogs.Dialog
import com.org.egglog.presentation.component.atoms.dropdown.SearchDropDownHospital
import com.org.egglog.presentation.component.atoms.inputs.SingleInput
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.domain.auth.activity.LoginActivity
import com.org.egglog.presentation.domain.setting.viewmodel.MySettingSideEffect
import com.org.egglog.presentation.domain.setting.viewmodel.MySettingViewModel
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.addFocusCleaner
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import kotlinx.coroutines.flow.Flow
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun MySettingScreen(
    viewModel: MySettingViewModel = hiltViewModel(),
    onNavigateToSettingScreen: () -> Unit
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MySettingSideEffect.Toast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            MySettingSideEffect.NavigateToLoginActivity -> {
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

    MySettingScreen(
        onNavigateToSettingScreen = onNavigateToSettingScreen,
        enabledModify = state.enabledModify,
        enabledDelete = state.enabledDelete,
        user = state.user,
        name = state.name,
        hospital = state.hospital,
        empNo = state.empNo,
        hospitalsFlow = state.hospitalsFlow,
        onNameChange = viewModel::onNameChange,
        onSearchChange = viewModel::onSearchChange,
        onHospitalSelected = viewModel::onHospitalSelected,
        onEmpNoChange = viewModel::onEmpNoChange,
        onClickDone = viewModel::onClickDone,
        onClickDelete = viewModel::onClickDelete,
        onClickModify = viewModel::onClickModify,
        search = state.search
    )
}

@Composable
fun MySettingScreen(
    onNavigateToSettingScreen: () -> Unit,
    enabledModify: Boolean,
    enabledDelete: Boolean,
    user: UserDetail?,
    onSearchChange: (String) -> Unit,
    name: String,
    hospital: UserHospital?,
    empNo: String,
    hospitalsFlow: Flow<PagingData<UserHospital>>,
    onNameChange: (String) -> Unit,
    onHospitalSelected: (UserHospital) -> Unit,
    onEmpNoChange: (String) -> Unit,
    onClickDone: () -> Unit,
    onClickDelete: () -> Unit,
    onClickModify: () -> Unit,
    search: String
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }

    Surface {
        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .addFocusCleaner(focusManager)
        ) {
            BasicHeader(
                title = "내 정보 수정",
                hasTitle = true,
                hasArrow = true,
                hasProgressBar = true,
                onClickBack = onNavigateToSettingScreen,
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
                Column(
                    Modifier.fillMaxWidth().padding(vertical = 30.heightPercent(context).dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ProfileCard(user)
                }

                Text(text = "이름 수정", style = Typography.headlineMedium)
                Text(text = "수정하신 이름은 그룹원에게 보입니다.", style = Typography.displayMedium, color = Gray400)
                Spacer(modifier = Modifier.height(16.heightPercent(context).dp))
                SingleInput(
                    modifier = Modifier.fillMaxWidth(),
                    text = name,
                    onValueChange = onNameChange,
                    focusManager = focusManager,
                    placeholder = "이름 입력",
                )
                Spacer(modifier = Modifier.height(30.heightPercent(context).dp))

                Text(text = "병원(근무지) 입력", style = Typography.headlineMedium)
                Text(text = "현재 근무 하시는 병원(근무지)을 입력해 주세요", style = Typography.displayMedium, color = Gray400)
                Spacer(modifier = Modifier.height(16.heightPercent(context).dp))
                SearchDropDownHospital(
                    list = hospitalsFlow.collectAsLazyPagingItems(),
                    placeholder = hospital?.hospitalName ?: "Select Hospital",
                    onSearchChange = onSearchChange,
                    onSelected = onHospitalSelected,
                    onClickDone = onClickDone,
                    search = search,
                    defaultText = hospital?.hospitalName ?: ""
                )
                Spacer(modifier = Modifier.height(30.heightPercent(context).dp))

                Text(text = "사번 입력", style = Typography.headlineMedium)
                Text(text = "현재 근무 근무지의 사번은 입력해 주세요", style = Typography.displayMedium, color = Gray400)
                Spacer(modifier = Modifier.height(16.heightPercent(context).dp))
                SingleInput(
                    modifier = Modifier.fillMaxWidth(),
                    text = empNo,
                    onValueChange = onEmpNoChange,
                    focusManager = focusManager,
                    placeholder = "사번 입력",
                )
                Spacer(modifier = Modifier.height(40.heightPercent(context).dp))

                Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
                    BigButton(
                        colors = ButtonColors(
                            containerColor = Warning300,
                            contentColor = NaturalWhite,
                            disabledContainerColor = Gray300,
                            disabledContentColor = NaturalWhite
                        ),
                        enabled = ((empNo.isNotEmpty() && hospital != null && name.isNotEmpty()) &&
                                (empNo != user?.empNo || hospital.hospitalName != user.selectedHospital?.hospitalName || name != user.userName) && enabledModify),
                        onClick = { onClickModify() }
                    ) {
                        Text(
                            text = if(empNo.isEmpty() || hospital == null || name.isEmpty()) "빈 값이 존재합니다"
                            else if(empNo == user?.empNo && hospital.hospitalName == user.selectedHospital?.hospitalName && name == user.userName) "수정된 값이 없습니다"
                            else if(!enabledModify) ""
                            else "수정하기"
                            , style = Typography.bodyMedium, color = NaturalWhite)
                    }
                }

                Spacer(modifier = Modifier.height(20.heightPercent(context).dp))
                HorizontalDivider(color = Gray200)
                Spacer(modifier = Modifier.height(20.heightPercent(context).dp))
                Text(text = "회원 탈퇴", style = Typography.headlineMedium)
                Text(text = "회원 탈퇴 시 계정의 모든 정보가 삭제되며, 취소할 수 없습니다.", style = Typography.displayMedium, color = Gray400)
                Spacer(modifier = Modifier.height(14.heightPercent(context).dp))
                Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
                    BigButton(
                        colors = ButtonColors(
                            containerColor = Gray400,
                            contentColor = NaturalWhite,
                            disabledContainerColor = Gray400,
                            disabledContentColor = NaturalWhite
                        ),
                        onClick = { openDialog.value = true }
                    ) {
                        Text(text = "회원 탈퇴", style = Typography.bodyMedium, color = NaturalWhite)
                    }
                }
                Spacer(modifier = Modifier.height(14.heightPercent(context).dp))

                when {
                    openDialog.value -> {
                        Dialog(
                            onDismissRequest = { openDialog.value = false },
                            onConfirmation = { onClickDelete() },
                            dialogTitle = "정말 탈퇴하시겠습니까?",
                            dialogText = "이 작업은 되돌릴 수 없습니다.",
                            enabled = enabledDelete,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun MySettingScreenPreview() {
    ClientTheme {
        MySettingScreen(
            onNavigateToSettingScreen = { }
        )
    }
}