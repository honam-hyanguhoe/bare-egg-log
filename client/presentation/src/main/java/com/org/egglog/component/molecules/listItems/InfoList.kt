package com.org.egglog.component.molecules.listItems

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.utils.Agree
import com.org.egglog.utils.Ask
import com.org.egglog.utils.Logout
import com.org.egglog.utils.MySetting
import com.org.egglog.utils.Notification
import com.org.egglog.utils.PersonalSetting
import com.org.egglog.utils.WorkSetting
import com.org.egglog.utils.widthPercent
import com.org.egglog.component.atoms.buttons.SettingButton
import com.org.egglog.theme.*

@Composable
fun InfoList() {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth().padding(10.widthPercent(context).dp)) {
        SettingButton(onClick = { Log.d("test: ", "clicked!!!")}, text =  "내 정보 설정", color = Gray600, icon = MySetting)
        SettingButton(onClick = { Log.d("test: ", "clicked!!!")}, text =  "알림 설정", color = Gray600, icon = Notification)
        SettingButton(onClick = { Log.d("test: ", "clicked!!!")}, text =  "근무 설정", color = Gray600, icon = WorkSetting)
        SettingButton(onClick = { Log.d("test: ", "clicked!!!")}, text =  "개인정보 처리방침", color = Gray600, icon = PersonalSetting)
        SettingButton(onClick = { Log.d("test: ", "clicked!!!")}, text =  "이용약관", color = Gray600, icon = Agree)
        SettingButton(onClick = { Log.d("test: ", "clicked!!!")}, text =  "문의하기", color = Gray600, icon = Ask)
        SettingButton(onClick = { Log.d("test: ", "clicked!!!")}, text =  "로그아웃", color = Error500, icon = Logout)
    }
}