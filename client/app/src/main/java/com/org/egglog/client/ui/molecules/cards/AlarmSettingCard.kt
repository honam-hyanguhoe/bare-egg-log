package com.org.egglog.client.ui.molecules.cards

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
import com.org.egglog.client.ui.atoms.cards.BackgroundCard
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.utils.widthPercent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.org.egglog.client.R
import com.org.egglog.client.data.ScheduleInfo
import com.org.egglog.client.ui.atoms.imageLoader.LocalImageLoader
import com.org.egglog.client.ui.atoms.toggle.Toggle
import com.org.egglog.client.ui.theme.DayCard
import com.org.egglog.client.ui.theme.Error300
import com.org.egglog.client.ui.theme.EveCard
import com.org.egglog.client.ui.theme.Gray100
import com.org.egglog.client.ui.theme.NightCard
import com.org.egglog.client.ui.theme.Orange300
import com.org.egglog.client.ui.theme.Pink300
import com.org.egglog.client.ui.theme.Primary400

@Composable
fun AlarmSettingCard(work: String, time: String, duration: Number, interval: Number, checked: Boolean, setToggle: () -> Unit, onClickCard: () -> Unit,color: Color? = Gray100) {
    val context = LocalContext.current

    val hour = time.substringBefore(":").toInt()
    val title = "${work} 근무 자동 알람 설정"

    val cardContent: ScheduleInfo = when(work) {
        "Day" -> ScheduleInfo(DayCard, title, R.drawable.day)
        "Eve" -> ScheduleInfo(EveCard, title, R.drawable.eve)
        "Night" -> ScheduleInfo(NightCard, title, R.drawable.night)
        "Off" -> ScheduleInfo(Primary400, title, R.drawable.off)
        "교육" -> ScheduleInfo(Orange300, title, R.drawable.education)
        "휴가" -> ScheduleInfo(Error300, title, R.drawable.vacation)
        "보건" -> ScheduleInfo(Pink300, title, R.drawable.health)
        else -> ScheduleInfo(color ?: Gray100, title, "")
    }

    BackgroundCard(margin = 4.widthPercent(context).dp, padding = 10.widthPercent(context).dp, color = cardContent.color, borderRadius = 10.widthPercent(context).dp) {
        Column() {
            Row(    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                Text("${cardContent.title}", style = Typography.bodyMedium.copy(fontSize = 18.sp))
                Toggle(checked = checked) {
                    setToggle()
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${if(hour in 0..11) "오전" else "오후"} ${time} | ${duration}분 간 | ${interval}분 간격", style = Typography.displayLarge)
                LocalImageLoader(imageUrl = cardContent.imageName, Modifier.size(44.dp))
            }
        }
    }
}