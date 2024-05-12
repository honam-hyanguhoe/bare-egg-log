package com.org.egglog.presentation.component.molecules.listItems

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.utils.Agree
import com.org.egglog.presentation.utils.Ask
import com.org.egglog.presentation.utils.Logout
import com.org.egglog.presentation.utils.MySetting
import com.org.egglog.presentation.utils.Notification
import com.org.egglog.presentation.utils.PersonalSetting
import com.org.egglog.presentation.utils.WorkSetting
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.buttons.SettingButton
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.Calendar

@Composable
fun InfoList(
    onClickPrepared: () -> Unit = {},
    onNavigateToPrivacyDetailScreen: () -> Unit,
    onNavigateToAgreeDetailScreen: () -> Unit,
    onNavigateToMySettingScreen: () -> Unit,
    onNavigateToCalendarSettingScreen: () -> Unit,
    onNavigateToWorkSettingScreen: () -> Unit,
    onClickLogout: () -> Unit
) {
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.widthPercent(context).dp)) {
        SettingButton(onClick = { onNavigateToMySettingScreen() }, text =  "내 정보 설정", color = Gray600, icon = MySetting)
        SettingButton(onClick = { onClickPrepared() }, text =  "알림 설정", color = Gray600, icon = Notification)
        SettingButton(onClick = { onNavigateToWorkSettingScreen() }, text =  "근무 설정", color = Gray600, icon = WorkSetting)
        SettingButton(onClick = { onNavigateToCalendarSettingScreen() }, text =  "캘린더 설정", color = Gray600, icon = Calendar)
        SettingButton(onClick = { onNavigateToPrivacyDetailScreen() }, text =  "개인정보 처리방침", color = Gray600, icon = PersonalSetting)
        SettingButton(onClick = { onNavigateToAgreeDetailScreen() }, text =  "이용약관", color = Gray600, icon = Agree)
        SettingButton(onClick = { onClickPrepared()  }, text =  "문의하기", color = Gray600, icon = Ask)
        SettingButton(onClick = { onClickLogout() }, text =  "로그아웃", color = Error500, icon = Logout)
    }
}