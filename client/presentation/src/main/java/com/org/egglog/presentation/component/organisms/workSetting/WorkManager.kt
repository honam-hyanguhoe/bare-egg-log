package com.org.egglog.presentation.component.organisms.workSetting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.component.molecules.cards.SmallScheduleCard
import com.org.egglog.presentation.utils.widthPercent

@Composable
fun WorkManager() {
    val context = LocalContext.current
    Column(
        Modifier
            .padding(horizontal = 8.widthPercent(context).dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SmallScheduleCard(work = "", startTime = "", endTime = "", onClickMore={})
        SmallScheduleCard(work = "", startTime = "", endTime = "", onClickMore={})
        SmallScheduleCard(work = "", startTime = "", endTime = "", onClickMore={})
        SmallScheduleCard(work = "", startTime = "", endTime = "", onClickMore={})
        SmallScheduleCard(work = "", startTime = "", endTime = "", onClickMore={})
        SmallScheduleCard(work = "", startTime = "", endTime = "", onClickMore={})
        SmallScheduleCard(work = "", startTime = "", endTime = "", onClickMore={})
        SmallScheduleCard(work = "", startTime = "", endTime = "", onClickMore={})
        SmallScheduleCard(work = "", startTime = "", endTime = "", onClickMore={})
        SmallScheduleCard(work = "", startTime = "", endTime = "", onClickMore={})
        SmallScheduleCard(work = "", startTime = "", endTime = "", onClickMore={})
        SmallScheduleCard(work = "", startTime = "", endTime = "", onClickMore={})
        SmallScheduleCard(work = "", startTime = "", endTime = "", onClickMore={})
    }
}