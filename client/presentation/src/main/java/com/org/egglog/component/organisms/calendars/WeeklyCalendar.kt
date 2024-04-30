package com.org.egglog.component.organisms.calendars

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.egglog.component.organisms.calendars.weeklyData.WeeklyDataSource
import com.org.egglog.component.organisms.calendars.weeklyData.WeeklyUiModel
import com.org.egglog.utils.widthPercent
import com.org.egglog.component.atoms.labels.Labels
import com.org.egglog.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter


// type = group일 땐 날짜 이동 가능
// type = group이 아닐 땐 날짜 이동 불가능
@Composable
fun WeeklyCalendar(
    type: String,
    calendarUiModel: WeeklyUiModel,
    onDateClick: ((WeeklyUiModel.Date) -> Unit)? = null,
    onPrevClick: ((LocalDate) -> Unit)? = null,
    onNextClick: ((LocalDate) -> Unit)? = null,
    labels: List<String> ?= null
) {

    Column(Modifier
            .fillMaxWidth()
    ) {
        if (type.equals("group")) {
            Row{
                CalendarHeader(calendarUiModel,onPrevClick, onNextClick)

                Spacer(modifier = Modifier.height(6.dp))
            }
        }
        Content(type, calendarUiModel, onDateClick, labels = labels)
    }

}

@Composable
fun CalendarHeader(
    data: WeeklyUiModel,
    onPrevClick: ((LocalDate) -> Unit)? = null,
    onNextClick: ((LocalDate) -> Unit)? = null
) {
    Row(Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
                text = data.selectedDate.date.format(DateTimeFormatter.ofPattern("yyyy.MM")),
                style = Typography.bodyLarge.copy(fontSize = 18.sp),
                modifier = Modifier
                        .align(Alignment.CenterVertically)
        )
        Row() {
            IconButton(onClick = { if(onPrevClick != null) onPrevClick(data.startDate.date) }) {
                Icon(
                        imageVector = Icons.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous",
                        Modifier.size(20.dp)
                )
            }
            IconButton(onClick = { if(onNextClick != null) onNextClick(data.endDate.date) }) {
                Icon(
                        imageVector = Icons.Filled.KeyboardArrowRight,
                        contentDescription = "Next",
                        Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun Content(type: String, data: WeeklyUiModel, onDateClick: ((WeeklyUiModel.Date) -> Unit)? = null, labels: List<String> ?= null) {

    Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
            data.visibleDates.forEachIndexed() { index, date ->
                DateItem(type, date, onDateClick, labels?.get(index) ?: "")
            }

    }
}


@Composable
fun DateItem(type: String,
             date: WeeklyUiModel.Date,
             onClick: ((WeeklyUiModel.Date) -> Unit)? = null,
             label: String? = null
) {
    Column() {
        Card(
                modifier = Modifier
                        .padding(vertical = 4.dp, horizontal = 2.dp)
                        .run {
                            if (type.equals("group")) {
                                clickable { // making the element clickable, by adding 'clickable' modifier
                                    if (onClick != null) {
                                        onClick(date)
                                    }
                                }
                            } else {
                                this
                            }
                        },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                        // background colors of the selected date
                        // and the non-selected date are different
                        containerColor = if (date.isSelected) {
                            Warning200
                        } else {
                            NaturalWhite
                        }
                ),
                border = if (date.isToday) {
                    BorderStroke(
                            width = 2.dp,
                            color = Warning200
                    )
                } else {
                    null
                }
        ) {
            Column(
                    modifier = Modifier
                            .width(38.widthPercent(LocalContext.current).dp)
                            .padding(vertical = 10.dp, horizontal = 4.dp),
                    verticalArrangement = Arrangement.Center
            ) {
                Text(
                        text = date.day.uppercase(),
                        color = if (date.day.equals("일")) Color(0xFFF97066) else if (date.isSelected) NaturalWhite else NaturalBlack,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = Typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                        text = date.date.dayOfMonth.toString(),
                        color = if (date.isSelected) NaturalWhite else NaturalBlack,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = Typography.displayMedium
                )
            }
        }
        if(type != "group") Labels(text = label ?: "None")
    }
}


@Preview(showBackground = true)
@Composable
fun WCTest() {
    MaterialTheme{
        Column {

            val dataSource = WeeklyDataSource()
            var calendarUiModel2 by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
            val labels: List<String> = listOf("Night", "Day", "휴가", "보건", "Off", "Eve", "Eve")

            WeeklyCalendar(type = "main", calendarUiModel2, labels = labels)
        }

    }
}