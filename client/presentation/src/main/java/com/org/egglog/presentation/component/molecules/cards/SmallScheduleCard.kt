package com.org.egglog.presentation.component.molecules.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.egglog.domain.setting.model.WorkType
import com.org.egglog.presentation.R
import com.org.egglog.presentation.data.ScheduleInfo
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.utils.MoreHoriz
import com.org.egglog.presentation.component.atoms.cards.BackgroundCard
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.component.atoms.menus.ScrollableMenus
import com.org.egglog.presentation.theme.*


@Composable
fun SmallScheduleCard(workType: WorkType, isMore: Boolean = false, groupOptions: List<String> = emptyList(), onSelected: (String, WorkType) -> Unit = { _, _ -> }) {
    val context = LocalContext.current

    val cardContent: ScheduleInfo = when(workType.workTag) {
        "DAY" -> ScheduleInfo(DayCard, "Day 근무", R.drawable.day)
        "EVE" -> ScheduleInfo(EveCard, "Eve 근무", R.drawable.eve)
        "NIGHT" -> ScheduleInfo(NightCard, "Night 근무", R.drawable.night)
        "OFF" -> ScheduleInfo(Primary400, "Off", R.drawable.off)
        "교육" -> ScheduleInfo(Orange300, "교육", R.drawable.education)
        "휴가" -> ScheduleInfo(Error300, "휴가", R.drawable.vacation)
        "보건" -> ScheduleInfo(Pink300, "보건", R.drawable.health)
        else -> ScheduleInfo(if(workType.color.isEmpty()) Gray200 else Color(android.graphics.Color.parseColor(workType.color)), workType.title, R.drawable.dark)
    }

    val selectedMenuItem by remember { mutableStateOf<String?>(null) }

    BackgroundCard(margin = 4.widthPercent(context).dp, padding = 12.widthPercent(context).dp, color = cardContent.color, borderRadius = 10.widthPercent(context).dp) {
        Column() {
            Row(Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(10.widthPercent(context).dp)
                            .background(Color.Transparent, CircleShape)
                            .border(3.dp, Primary600, CircleShape)) {}
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${workType.startTime.substring(0, 5)} - ${workType.workTime.substring(0, 5)}", style = Typography.displayLarge)
                }
                if(isMore) ScrollableMenus(
                    iconShape = MoreHoriz,
                    boxSize = 20,
                    iconSize = 20,
                    options = groupOptions,
                    selectedOption = selectedMenuItem,
                    onSelect = { onSelected(it, workType)}
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Row(Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = cardContent.title, style = Typography.bodyLarge.copy(fontSize = 18.sp))
                LocalImageLoader(imageUrl = cardContent.imageName, Modifier.size(36.dp))
            }
        }
    }
}

@Preview
@Composable
fun CardPreview() {
    MaterialTheme {
        fun onClickMore(planId: Any? = null) {
            println(planId)
        }
        SmallScheduleCard(
            WorkType(
                workTypeId = 0L,
                title = "",
                color = "",
                workTypeImgUrl = "",
                workTag = "",
                startTime = "",
                workTime = ""
            ), groupOptions = emptyList(), isMore = false)
    }
}