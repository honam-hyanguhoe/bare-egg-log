package com.org.egglog.client.ui.organisms.calendars

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
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
import com.org.egglog.client.ui.organisms.calendars.weeklyData.WeeklyDataSource
import com.org.egglog.client.ui.organisms.calendars.weeklyData.WeeklyUiModel
import com.org.egglog.client.ui.theme.NaturalBlack
import com.org.egglog.client.ui.theme.NaturalWhite
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.ui.theme.Warning200
import com.org.egglog.client.ui.theme.White
import com.org.egglog.client.utils.widthPercent
import java.time.LocalDate
import java.time.format.DateTimeFormatter


// type = group일 땐 날짜 이동 가능
// type = group이 아닐 땐 날짜 이동 불가능
@Composable
fun WeeklyCalendar(type: String) {
    val dataSource = WeeklyDataSource()
    var calendarUiModel by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }

    Column(Modifier
            .fillMaxWidth()
    ) {
        if (type.equals("group")) {
            Row{
                CalendarHeader(calendarUiModel,

                        onPrevClick = { startDate ->
                    val finalStartDate = startDate.minusDays(1)
                    calendarUiModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = finalStartDate)
                    // ** TODO **
                    // 클릭한 날짜에 대한 근무 인원 정보 받아오기
                    println("선택한 날짜는 ${finalStartDate}") },

                        onNextClick = { endDate ->
                    val finalStartDate = endDate.plusDays(2)
                    calendarUiModel = dataSource.getData(startDate = finalStartDate, lastSelectedDate = finalStartDate.minusDays(1))
                    // ** TODO **
                    // 클릭한 날짜에 대한 근무 인원 정보 받아오기
                    println("선택한 날짜는 ${finalStartDate.minusDays(1)}")
                })

                Spacer(modifier = Modifier.height(6.dp))
            }
        }
        Content(type, calendarUiModel, onDateClick = { date ->
            calendarUiModel = calendarUiModel
                    .copy(
                    selectedDate = date,
                    visibleDates = calendarUiModel.visibleDates.map {
                        it.copy(
                                isSelected = it.date.isEqual(date.date)
                        )
                    })
            // ** TODO **
            // 클릭한 날짜에 대한 근무 인원 정보 받아오기
            println("선택한 날짜는 ${date.date}")
        })
    }

}

@Composable
fun CalendarHeader(
        data: WeeklyUiModel,
        onPrevClick: (LocalDate) -> Unit,
        onNextClick: (LocalDate) -> Unit
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
            IconButton(onClick = { onPrevClick(data.startDate.date)}) {
                Icon(
                        imageVector = Icons.Filled.ChevronLeft,
                        contentDescription = "Previous",
                        Modifier.size(20.dp)
                )
            }
            IconButton(onClick = { onNextClick(data.endDate.date) }) {
                Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "Next",
                        Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun Content(type: String, data: WeeklyUiModel, onDateClick: (WeeklyUiModel.Date) -> Unit) {
    Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
        data.visibleDates.forEach() { date ->
            DateItem(type, date, onDateClick)
        }
    }
}


@Composable
fun DateItem(type: String,
             date: WeeklyUiModel.Date,
             onClick: (WeeklyUiModel.Date) -> Unit
) {
    Card(
            modifier = Modifier
                    .padding(vertical = 4.dp, horizontal = 2.dp)
                    .run {
                        if (type.equals("group")) {
                            clickable { // making the element clickable, by adding 'clickable' modifier
                                onClick(date)
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
                    color = if(date.day.equals("Sun")) Color(0xFFF97066) else if(date.isSelected) NaturalWhite else NaturalBlack  ,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = Typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                    text = date.date.dayOfMonth.toString(),
                    color = if(date.isSelected) NaturalWhite else NaturalBlack,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = Typography.displayMedium
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WCTest() {
    MaterialTheme{
        Row {
            WeeklyCalendar(type = "group")
            WeeklyCalendar(type = "main")
        }

    }
}