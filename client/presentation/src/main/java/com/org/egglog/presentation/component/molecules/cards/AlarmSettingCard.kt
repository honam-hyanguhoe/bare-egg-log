package com.org.egglog.presentation.component.molecules.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.org.egglog.presentation.utils.widthPercent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.org.egglog.domain.setting.model.Alarm
import com.org.egglog.presentation.R
import com.org.egglog.presentation.data.ScheduleInfo
import com.org.egglog.presentation.component.atoms.cards.BackgroundCard
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.component.atoms.toggle.Toggle
import com.org.egglog.presentation.theme.*
import java.time.format.DateTimeFormatter

@Composable
fun AlarmSettingCard(alarm: Alarm, setToggle: (Alarm) -> Unit, onClickCard: () -> Unit) {
    val context = LocalContext.current
    val title = "${alarm.workTypeTitle} 근무 자동 알람 설정"
    val formatter = DateTimeFormatter.ofPattern("a hh:mm")
    val timeString = alarm.alarmTime.format(formatter)

    val cardContent: ScheduleInfo = when(alarm.workTypeTitle) {
        "DAY" -> ScheduleInfo(DayCard, title, R.drawable.day)
        "EVE" -> ScheduleInfo(EveCard, title, R.drawable.eve)
        "NIGHT" -> ScheduleInfo(NightCard, title, R.drawable.night)
        "OFF" -> ScheduleInfo(Primary400, title, R.drawable.off)
        "교육" -> ScheduleInfo(Orange300, title, R.drawable.education)
        "휴가" -> ScheduleInfo(Error300, title, R.drawable.vacation)
        "보건" -> ScheduleInfo(Pink300, title, R.drawable.health)
        else -> ScheduleInfo(if(alarm.workTypeColor.isNullOrEmpty()) Gray100 else Color(android.graphics.Color.parseColor(alarm.workTypeColor)), title, "")
    }

    BackgroundCard(margin = 4.widthPercent(context).dp, padding = 10.widthPercent(context).dp, color = cardContent.color, borderRadius = 10.widthPercent(context).dp, onClickCard = onClickCard) {
        Column() {
            Row(    Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Text(cardContent.title, style = Typography.bodyLarge.copy(fontSize = 18.sp))
                Toggle(checked = alarm.isAlarmOn) {
                    setToggle(alarm)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text("$timeString | ${alarm.replayTime}분 간격 | ${alarm.replayCnt}번 반복", style = Typography.displayLarge)
                LocalImageLoader(imageUrl = cardContent.imageName, Modifier.size(44.dp))
            }
        }
    }
}