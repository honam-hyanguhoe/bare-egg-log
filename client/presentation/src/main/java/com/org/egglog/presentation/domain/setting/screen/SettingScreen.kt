package com.org.egglog.presentation.domain.setting.screen

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.R
import com.org.egglog.presentation.component.atoms.buttons.BasicButton
import com.org.egglog.presentation.component.atoms.dialogs.Dialog
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.component.molecules.listItems.InfoList
import com.org.egglog.presentation.utils.ArrowRight
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.presentation.component.molecules.bottomNavigator.BottomNavigator
import com.org.egglog.presentation.domain.auth.activity.LoginActivity
import com.org.egglog.presentation.domain.community.activity.CommunityActivity
import com.org.egglog.presentation.domain.group.activity.GroupActivity
import com.org.egglog.presentation.domain.main.activity.MainActivity
import com.org.egglog.presentation.domain.setting.viewmodel.SettingSideEffect
import com.org.egglog.presentation.domain.setting.viewmodel.SettingViewModel
import com.org.egglog.presentation.utils.getVersionInfo
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SettingScreen(
    viewModel: SettingViewModel = hiltViewModel(),
    onNavigateToPrivacyDetailScreen: () -> Unit,
    onNavigateToAgreeDetailScreen: () -> Unit,
    onNavigateToCalendarSettingScreen: () -> Unit,
    onNavigateToMySettingScreen: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.collectAsState().value
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SettingSideEffect.Toast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            SettingSideEffect.NavigateToMainActivity -> {
                context.startActivity(
                    Intent(
                        context, MainActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
            SettingSideEffect.NavigateToLoginActivity -> {
                context.startActivity(
                    Intent(
                        context, LoginActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
            SettingSideEffect.NavigateToCommunityActivity -> {
                context.startActivity(
                    Intent(
                        context, CommunityActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
            SettingSideEffect.NavigateToGroupActivity -> {
                context.startActivity(
                    Intent(
                        context, GroupActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }
        }
    }

    SettingScreen(
        onNavigateToPrivacyDetailScreen = onNavigateToPrivacyDetailScreen,
        onNavigateToAgreeDetailScreen = onNavigateToAgreeDetailScreen,
        onSelectedIdx = viewModel::onSelectedIdx,
        onNavigateToCalendarSettingScreen = onNavigateToCalendarSettingScreen,
        onNavigateToMySettingScreen = onNavigateToMySettingScreen,
        onClickLogout = viewModel::onClickLogout,
        logoutEnabled = state.logoutEnabled,
        selectedIdx = state.selectedIdx
    )
}

@Composable
fun SettingScreen(
    onNavigateToPrivacyDetailScreen: () -> Unit,
    onNavigateToAgreeDetailScreen: () -> Unit,
    onNavigateToMySettingScreen: () -> Unit,
    onNavigateToCalendarSettingScreen: () -> Unit,
    onClickLogout: () -> Unit,
    logoutEnabled: Boolean,
    selectedIdx: Int,
    onSelectedIdx: (Int) -> Unit

) {
    val context = LocalContext.current
    val openDialog = remember { mutableStateOf(false) }
    val openLogoutDialog = remember { mutableStateOf(false) }
    val version = getVersionInfo(context)

    fun onClickLogoutConfirm() {
        onClickLogout()
        openLogoutDialog.value = false
    }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(18.heightPercent(context).dp))
            Text(text = "마이페이지", style = Typography.headlineSmall)
            Spacer(modifier = Modifier.height(18.heightPercent(context).dp))
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.92f)
                    .padding(vertical = 6.widthPercent(context).dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                // 인증 배지 신청하기 버튼
                BasicButton(modifier = Modifier
                    .height(68.heightPercent(context).dp)
                    .fillMaxWidth(0.94f),
                    onClick = { openDialog.value = true },
                    shape = RoundedCornerShape(16.widthPercent(context).dp),
                    colors = ButtonColors(
                    containerColor = Warning300,
                    contentColor = NaturalWhite,
                    disabledContainerColor = Gray300,
                    disabledContentColor = NaturalWhite
                )) {
                    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically,) {
                            LocalImageLoader(imageUrl = R.drawable.small_fry, Modifier.size(32.widthPercent(context).dp))
                            Spacer(modifier = Modifier.padding(4.widthPercent(context).dp))
                            Column {
                                Text(text = "인증 배지 신청하기", style = Typography.bodyLarge)
                                Text(text = "면허를 인증할 수 있어요", style = Typography.displayMedium)
                            }
                        }
                        Icon(imageVector = ArrowRight, modifier = Modifier.size(24.widthPercent(context).dp), color = NaturalWhite)
                    }
                }
                InfoList(
                    onClickPrepared = { openDialog.value = true },
                    onNavigateToPrivacyDetailScreen = onNavigateToPrivacyDetailScreen,
                    onNavigateToAgreeDetailScreen = onNavigateToAgreeDetailScreen,
                    onNavigateToMySettingScreen = onNavigateToMySettingScreen,
                    onNavigateToCalendarSettingScreen = onNavigateToCalendarSettingScreen,
                    onClickLogout = { openLogoutDialog.value = true }
                )

                Spacer(modifier = Modifier.padding(30.heightPercent(context).dp))
                LocalImageLoader(imageUrl = R.drawable.bottom_logo, Modifier.fillMaxWidth(0.52f))
                Text("Version $version", style = Typography.displayMedium.copy(color = Gray400))

                when {
                    openDialog.value -> {
                        Dialog(
                            onDismissRequest = { openDialog.value = false },
                            onConfirmation = { openDialog.value = false },
                            dialogTitle = "공지",
                            dialogText = "출시 준비 중입니다",
                        )
                    }
                }

                when {
                    openLogoutDialog.value -> {
                        Dialog(
                            enabled = logoutEnabled,
                            onDismissRequest = { openLogoutDialog.value = false },
                            onConfirmation = { onClickLogoutConfirm() },
                            dialogTitle = "로그아웃 하시겠습니까?",
                            dialogText = "로그아웃 시 로그인 화면으로 돌아갑니다.",
                        )
                    }
                }
            }

            Box(modifier = Modifier.fillMaxHeight(1f).fillMaxWidth()) {
                BottomNavigator(selectedItem = selectedIdx, onItemSelected = { onSelectedIdx(it) })
            }
        }
    }
}

@Preview
@Composable
private fun SettingScreenPreview() {
    ClientTheme {
        SettingScreen(
            onNavigateToPrivacyDetailScreen = {},
            onNavigateToAgreeDetailScreen = {},
            onNavigateToMySettingScreen = {},
            onNavigateToCalendarSettingScreen = {}
        )
    }
}