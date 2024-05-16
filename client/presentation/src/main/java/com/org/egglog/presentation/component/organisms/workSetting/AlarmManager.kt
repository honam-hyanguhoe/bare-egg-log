package com.org.egglog.presentation.component.organisms.workSetting

import android.app.AlarmManager
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.component.molecules.cards.AlarmSettingCard
import com.org.egglog.presentation.receiver.AlarmConst
import com.org.egglog.presentation.utils.widthPercent
import java.time.LocalTime

@Composable
fun AlarmManager(alarmManager: AlarmManager) {
    val context = LocalContext.current
    AlarmConst.setAlarm(context = context, alarmManager = alarmManager, curRepeatCount = 0, repeatCount = 3, time = LocalTime.of(13, 15), minutesToAdd = 5L)
    AlarmConst.setAlarm(context = context, alarmManager = alarmManager, curRepeatCount = 0, repeatCount = 5, time = LocalTime.of(13, 17), minutesToAdd = 10L)
    AlarmConst.setAlarm(context = context, alarmManager = alarmManager, curRepeatCount = 0, repeatCount = 3, time = LocalTime.of(13, 19), minutesToAdd = 2L)
    Column(
        Modifier
            .padding(horizontal = 8.widthPercent(context).dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AlarmSettingCard(
            work = "",
            time = "1",
            duration = 1,
            interval = 5,
            checked = false,
            setToggle = { },
            onClickCard = { },
            color = null
        )
    }
}