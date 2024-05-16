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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.egglog.domain.myCalendar.model.EventListData
import com.org.egglog.presentation.data.ScheduleInfo
import com.org.egglog.presentation.utils.MoreHoriz
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.cards.BackgroundCard
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.R
import com.org.egglog.presentation.component.atoms.menus.ScrollableMenus

@Composable
fun BigScheduleCard(eventData: EventListData, color: Color? = Gray100, onClickDelete: () -> Unit = {}, onClickModify: () -> Unit = {}) {
    val context = LocalContext.current

    var selectedMenuItem by remember { mutableStateOf<String?>(null) }

    val cardContent: ScheduleInfo = when(eventData.eventTitle) {
        "day" -> ScheduleInfo(DayCard, "Day 근무", R.drawable.day)
        "eve" -> ScheduleInfo(EveCard, "Eve 근무", R.drawable.eve)
        "night" -> ScheduleInfo(NightCard, "Night 근무", R.drawable.night)
        "off" -> ScheduleInfo(Primary400, "Off", R.drawable.off)
        "교육" -> ScheduleInfo(Orange300, "교육", R.drawable.education)
        "휴가" -> ScheduleInfo(Error300, "휴가", R.drawable.vacation)
        "보건" -> ScheduleInfo(Pink300, "보건", R.drawable.health)
        else -> ScheduleInfo(color ?: Gray100, eventData.eventTitle ?: "", "")
    }

    BackgroundCard(margin = 4.widthPercent(context).dp, padding = 12.widthPercent(context).dp, color = cardContent.color, borderRadius = 10.widthPercent(context).dp) {
        Column() {
            Row(Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier
                            .size(10.widthPercent(context).dp)
                            .background(Color.Transparent, CircleShape)
                            .border(3.dp, Primary600, CircleShape)) {}
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "${eventData.startDate.substring(11,16)} - ${eventData.endDate.substring(11,16)}", style = Typography.displayLarge)
                }
                ScrollableMenus(
                    iconShape = MoreHoriz,
                    boxSize = 20,
                    iconSize = 20,
                    options = listOf("수정하기", "삭제하기"),
                    selectedOption = selectedMenuItem,
                    onSelect = {
                        selectedMenuItem = it
                        if(selectedMenuItem == "삭제하기") {
                            onClickDelete()
                        } else if(selectedMenuItem == "수정하기") {
                            onClickModify()
                        }
                     }
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(Modifier
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
            Column() {
                Text(text = cardContent.title, style = Typography.bodyLarge.copy(fontSize = 18.sp))
                Spacer(modifier = Modifier.height(6.dp))
                    Text(eventData.eventContent ?: "", style = Typography.displayMedium)
                }
                if(cardContent.imageName != "") {
                    LocalImageLoader(imageUrl = cardContent.imageName, Modifier.size(42.widthPercent(context).dp))
                }
            }
        }
    }
}