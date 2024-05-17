package com.org.egglog.presentation.component.molecules.listItems

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.org.egglog.presentation.utils.Agree
import com.org.egglog.presentation.utils.Ask
import com.org.egglog.presentation.utils.Logout
import com.org.egglog.presentation.utils.MySetting
import com.org.egglog.presentation.utils.Notification
import com.org.egglog.presentation.utils.PersonalSetting
import com.org.egglog.presentation.utils.WorkSetting
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.buttons.SettingButton
import com.org.egglog.presentation.component.atoms.dialogs.Dialog
import com.org.egglog.presentation.domain.setting.activity.SettingActivity
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.Calendar

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun InfoList(
    onNavigateToPrivacyDetailScreen: () -> Unit,
    onNavigateToAgreeDetailScreen: () -> Unit,
    onNavigateToMySettingScreen: () -> Unit,
    onNavigateToCalendarSettingScreen: () -> Unit,
    onNavigateToAskSettingScreen: () -> Unit,
    onNavigateToWorkSettingScreen: () -> Unit,
    onNavigateToNotificationSettingScreen: () -> Unit,
    onClickLogout: () -> Unit
) {
    val context = LocalContext.current
    val contextActivity = LocalContext.current as SettingActivity
    val openDialog = remember { mutableStateOf(false) }
    val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    val (hasPermission, setHasPermission) = remember { mutableStateOf(notificationPermissionState.status.isGranted) }

    LaunchedEffect(notificationPermissionState.status) {
        setHasPermission(notificationPermissionState.status.isGranted)
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.widthPercent(context).dp)) {
        SettingButton(onClick = { onNavigateToMySettingScreen() }, text =  "내 정보 설정", color = Gray600, icon = MySetting)
        SettingButton(onClick = {
            if (!hasPermission) {
                openDialog.value = true
            } else {
                onNavigateToNotificationSettingScreen()
            }
        }, text = "알림 설정", color = Gray600, icon = Notification)
        SettingButton(onClick = { onNavigateToWorkSettingScreen() }, text = "근무 설정", color = Gray600, icon = WorkSetting)
        SettingButton(onClick = { onNavigateToCalendarSettingScreen() }, text = "캘린더 설정", color = Gray600, icon = Calendar)
        SettingButton(onClick = { onNavigateToPrivacyDetailScreen() }, text = "개인정보 처리방침", color = Gray600, icon = PersonalSetting)
        SettingButton(onClick = { onNavigateToAgreeDetailScreen() }, text = "이용약관", color = Gray600, icon = Agree)
        SettingButton(onClick = { onNavigateToAskSettingScreen()  }, text = "문의하기", color = Gray600, icon = Ask)
        SettingButton(onClick = { onClickLogout() }, text =  "로그아웃", color = Error500, icon = Logout)
        when {
            openDialog.value -> {
                Dialog(
                    onDismissRequest = { openDialog.value = false },
                    onConfirmation = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", contextActivity.packageName, null)
                        }
                        context.startActivity(intent)
                        openDialog.value = false
                    },
                    dialogTitle = "알람 권한이 필요합니다.",
                    dialogText = "확인 시 설정 창으로 이동합니다.",
                )
            }
        }
    }
}