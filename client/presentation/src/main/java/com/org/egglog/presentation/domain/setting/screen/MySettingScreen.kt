package com.org.egglog.presentation.domain.setting.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.org.egglog.client.data.UserInfo
import com.org.egglog.domain.auth.model.UserHospital
import com.org.egglog.presentation.component.atoms.buttons.BigButton
import com.org.egglog.presentation.component.atoms.cards.ProfileCard
import com.org.egglog.presentation.component.atoms.dropdown.SearchDropDownHospital
import com.org.egglog.presentation.component.atoms.inputs.SingleInput
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.domain.setting.viewmodel.MySettingViewModel
import com.org.egglog.presentation.domain.setting.viewmodel.SettingViewModel
import com.org.egglog.presentation.theme.ClientTheme
import com.org.egglog.presentation.theme.Gray300
import com.org.egglog.presentation.theme.Gray400
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.theme.Warning300
import com.org.egglog.presentation.utils.heightPercent
import kotlinx.coroutines.flow.Flow
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun MySettingScreen(
    viewModel: MySettingViewModel = hiltViewModel(),
    onNavigateToSettingScreen: () -> Unit
) {
    val state = viewModel.collectAsState().value

    MySettingScreen(
        onNavigateToSettingScreen = onNavigateToSettingScreen,
        name = state.name,
        hospital = state.hospital,
        empNo = state.empNo,
        hospitalsFlow = state.hospitalsFlow,
        onNameChange = viewModel::onNameChange,
        onSearchChange = viewModel::onSearchChange,
        onHospitalSelected = viewModel::onHospitalSelected,
        onEmpNoChange = viewModel::onEmpNoChange,
//        onClickDone = viewModel::onClickDone,
        onClickDone = {},
        search = state.search
    )
}

@Composable
fun MySettingScreen(
    onNavigateToSettingScreen: () -> Unit,
    onSearchChange: (String) -> Unit,
    name: String,
    hospital: UserHospital?,
    empNo: String,
    hospitalsFlow: Flow<PagingData<UserHospital>>,
    onNameChange: (String) -> Unit,
    onHospitalSelected: (UserHospital) -> Unit,
    onEmpNoChange: (String) -> Unit,
    onClickDone: () -> Unit,
    search: String
) {
    val focusManager = LocalFocusManager.current
    Surface {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
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

            ProfileCard(userInfo = UserInfo(
                profileImgUrl = "https://www.google.com/#q=sollicitudin",
                userName = "Phil Frost",
                empNo = "dictas",
                userEmail = "lucio.gaines@example.com",
                userId = 7324
            ))

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
            SearchDropDownHospital(
                list = hospitalsFlow.collectAsLazyPagingItems(),
                placeholder = "Select Hospital",
                onSearchChange = onSearchChange,
                onSelected = onHospitalSelected,
                onClickDone = onClickDone,
                search = search
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
            Spacer(modifier = Modifier.height(40.heightPercent(LocalContext.current).dp))
            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
                BigButton(
                    colors = ButtonColors(
                        containerColor = Warning300,
                        contentColor = NaturalWhite,
                        disabledContainerColor = Gray300,
                        disabledContentColor = NaturalWhite
                    ),
                    enabled = empNo.isNotEmpty() && hospital != null && name.isNotEmpty(),
                    onClick = {}
                ) {
                    Text(text = "수정하기", style = Typography.bodyMedium, color = NaturalWhite)
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