package com.org.egglog.presentation.component.organisms.workSetting

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.domain.setting.model.Alarm
import com.org.egglog.presentation.component.atoms.dialogs.BottomSheet
import com.org.egglog.presentation.component.molecules.cards.AlarmSettingCard
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import java.time.LocalTime

@Composable
fun AlarmManager(
    alarmList: List<Alarm>,
    onClickToggle: (Alarm) -> Unit,
    showModifyBottomSheetAlarm: Boolean,
    modifyEnabledAlarm: Boolean,
    setShowModifyBottomSheetAlarm: (Boolean) -> Unit,
    setSelectedAlarm: (Alarm) -> Unit,
    deleteSelectedAlarm: () -> Unit,
    onReplayCntChange: (Int) -> Unit,
    onReplayTimeChange: (Int) -> Unit,
    onClickModifyAlarm: () -> Unit,
    onAlarmTimeChange: (LocalTime) -> Unit
) {
    val context = LocalContext.current
    Column(
        Modifier
            .padding(horizontal = 8.widthPercent(context).dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(6.heightPercent(context).dp))
        alarmList.map {
            AlarmSettingCard(
                alarm = it,
                onClickCard = {
                    setShowModifyBottomSheetAlarm(true)
                    setSelectedAlarm(it)
                },
                setToggle = onClickToggle
            )
        }
        Spacer(modifier = Modifier.height(6.heightPercent(context).dp))

        if(showModifyBottomSheetAlarm) BottomSheet(height = 420.heightPercent(context).dp, showBottomSheet = true, onDismiss = {
            setShowModifyBottomSheetAlarm(false)
            onReplayTimeChange(5)
            onReplayCntChange(1)
            deleteSelectedAlarm()
        }) {
            AlarmModifySheet(
                onAlarmTimeChange = onAlarmTimeChange,
                addEnabled = modifyEnabledAlarm,
                onClickAdd = {
                    onClickModifyAlarm()
                    setShowModifyBottomSheetAlarm(false)
                },
                onReplayTimeChange = onReplayTimeChange,
                onReplayCntChange = onReplayCntChange
            )
        }
    }
}