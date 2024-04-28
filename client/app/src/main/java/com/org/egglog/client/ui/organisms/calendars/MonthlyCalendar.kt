package com.org.egglog.client.ui.organisms.calendars

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.org.egglog.client.ui.atoms.icons.Icon
import com.org.egglog.client.ui.atoms.labels.Labels
import com.org.egglog.client.ui.theme.Blue500
import com.org.egglog.client.ui.theme.Typography
import com.org.egglog.client.ui.theme.Error600
import com.org.egglog.client.ui.theme.Blue700
import com.org.egglog.client.ui.theme.BlueGray400
import com.org.egglog.client.ui.theme.Error200
import com.org.egglog.client.ui.theme.Gray200
import com.org.egglog.client.ui.theme.Gray400
import com.org.egglog.client.ui.theme.NaturalBlack
import com.org.egglog.client.utils.ArrowLeft
import com.org.egglog.client.utils.ArrowRight
import java.util.Calendar

@Composable
fun MonthlyCalendar(
        currentYear: MutableState<Int>,
        currentMonth: MutableState<Int>,
        onDateClicked: (Int) -> Unit,
        onPrevMonthClick: () -> Int,
        onNextMonthClick: () -> Int
) {


    Column(Modifier
            .fillMaxWidth()
    ) {
        CalendarHeader(currentYear, currentMonth, onPrevMonthClick, onNextMonthClick)
        Spacer(modifier = Modifier.height(16.dp))
        DayList(currentYear, currentMonth, onDateClicked = onDateClicked) // 콜백 전달
    }
}

@Composable
fun CalendarHeader(
        year: MutableState<Int>,
        month: MutableState<Int>,
        onPrevMonthClick: () -> Int,
        onNextMonthClick: () -> Int
) {
    val nameList = listOf("일", "월", "화", "수", "목", "금", "토")

    Column {
        Row(Modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onPrevMonthClick() }) {
                Icon(imageVector = ArrowLeft, modifier = Modifier.size(20.dp))
            }
            Text(text = "${year.value}년 ${month.value}월")
            IconButton(onClick = { onNextMonthClick() }) {
                Icon(imageVector = ArrowRight, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row() {
            nameList.forEach {
                Box(modifier = Modifier
                        .weight(1f),
                        contentAlignment = Alignment.Center) {
                    Text(
                            text = it,
                            style = Typography.displayMedium,
                            color = if(it.equals("일")) Error600 else if(it.equals("토")) Blue700 else NaturalBlack
                    )
                }
            }

        }
    }
    
}

@Composable
fun DayList(
        currentYear: MutableState<Int>,
        currentMonth: MutableState<Int>,
        onDateClicked: (Int) -> Unit
) {
    val time = remember { mutableStateOf(Calendar.getInstance()) }
    val date = time.value
    val selectedDate = remember { mutableStateOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) }

    // 시간 세팅
    date.set(Calendar.YEAR, currentYear.value)
    date.set(Calendar.MONTH, currentMonth.value - 1)
    date.set(Calendar.DAY_OF_MONTH, 1)

    // 이번달 정보
    val thisMonthDayMax = date.getActualMaximum(Calendar.DAY_OF_MONTH) // 최대 일 수
    val thisMonthFirstDay = date.get(Calendar.DAY_OF_WEEK) - 1 // 이번달 1일의 요일
    val thisMonthWeeksCount = (thisMonthDayMax + thisMonthFirstDay + 6) / 7 // 이번달 주 수

    // 저번달 정보
    val lastMonthDate = Calendar.getInstance()
    lastMonthDate.add(Calendar.MONTH, -1) // 이전 달로 이동
    val lastMonthDayMax = lastMonthDate.getActualMaximum(Calendar.DAY_OF_MONTH)

    Column {
        repeat(thisMonthWeeksCount) { week ->
            Row {
                repeat(7) { day ->
                    val resultDay = week * 7 + day - thisMonthFirstDay + 1
                    val isSelected = selectedDate.value == resultDay
                    val dayOfWeek = (day + 1) % 7

                    if (resultDay in 1..thisMonthDayMax) {
                        // 달력 날짜 범위 내
                        Box(
                                modifier = Modifier
                                        .weight(1f)
                                        .height(70.dp)
                                        .clickable {
                                            selectedDate.value = resultDay
                                            onDateClicked(resultDay)
                                        }
                                        .background(
                                                if (isSelected) Gray200 else Color.Transparent,
                                                RoundedCornerShape(10.dp)
                                        )
                                        .padding(4.dp),
                                contentAlignment = Alignment.TopCenter
                        ) {
                            Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                        text = resultDay.toString(),
                                        style = Typography.displayMedium,
                                        color = when (dayOfWeek) {
                                            Calendar.SUNDAY -> Error600
                                            0 -> Blue700
                                            else -> NaturalBlack
                                        }
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Labels(text = "Night")
                                Text(text = "··")
                            }
                        }
                    } else {
                        // 달력 날짜 범위 밖
                        Box(
                                modifier = Modifier
                                        .weight(1f)
                                        .height(70.dp)
                                        .background(
                                                if (isSelected) Gray200 else Color.Transparent,
                                                RoundedCornerShape(10.dp)
                                        )
                                        .padding(4.dp),
                                contentAlignment = Alignment.TopCenter
                        ) {
                            Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                        text = if (resultDay < 1) "${resultDay + lastMonthDayMax}" else "${resultDay - thisMonthDayMax}",
                                        color = when (dayOfWeek) {
                                            Calendar.SUNDAY -> Error200
                                            0 -> BlueGray400
                                            else -> Gray400
                                        },
                                        style = Typography.displayMedium
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Labels(text = "Night")
                                Text(text = "·")
                            }
                        }
                    }
                }
            }
        }
    }
}