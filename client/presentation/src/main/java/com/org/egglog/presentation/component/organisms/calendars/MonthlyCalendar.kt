package com.org.egglog.presentation.component.organisms.calendars

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.org.egglog.domain.myCalendar.model.PersonalScheduleData
import com.org.egglog.domain.myCalendar.model.WorkListData
import com.org.egglog.domain.myCalendar.model.WorkType
import com.org.egglog.presentation.utils.ArrowLeft
import com.org.egglog.presentation.utils.ArrowRight
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.component.atoms.labels.Labels
import com.org.egglog.presentation.component.atoms.labels.Labels2
import com.org.egglog.presentation.theme.*
import java.util.Calendar

@Composable
fun MonthlyCalendar(
    currentYear: Int,
    currentMonth: Int,
    onDateClicked: (Int) -> Unit,
    onPrevMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    selectedDate: Int,
    tempWorkList: List<Pair<Int, WorkType?>>,
    monthlyWorkList: List<WorkListData>,
    monthlyPersonalList: List<PersonalScheduleData>
) {

    Column(
        Modifier
            .fillMaxWidth()
    ) {
        CalendarHeader(currentYear, currentMonth, onPrevMonthClick, onNextMonthClick)
        Spacer(modifier = Modifier.height(16.dp))
        DayList(
            currentYear,
            currentMonth,
            onDateClicked = onDateClicked,
            selectedDate,
            tempWorkList = tempWorkList,
            monthlyWorkList = monthlyWorkList,
            monthlyPersonalList = monthlyPersonalList
        )
    }
}

@Composable
fun CalendarHeader(
    year: Int,
    month: Int,
    onPrevMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit
) {
    val nameList = listOf("일", "월", "화", "수", "목", "금", "토")

    Column {
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onPrevMonthClick() }) {
                Icon(imageVector = ArrowLeft, modifier = Modifier.size(20.dp))
            }
            Text(text = "${year}년 ${month}월")
            IconButton(onClick = { onNextMonthClick() }) {
                Icon(imageVector = ArrowRight, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row() {
            nameList.forEach {
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = it,
                        style = Typography.displayMedium,
                        color = if (it.equals("일")) Error600 else if (it.equals("토")) Blue700 else NaturalBlack
                    )
                }
            }

        }
    }

}

@Composable
fun DayList(
    currentYear: Int,
    currentMonth: Int,
    onDateClicked: (Int) -> Unit,
    selectedDate: Int,
    tempWorkList: List<Pair<Int, WorkType?>>,
    monthlyWorkList: List<WorkListData>,
    monthlyPersonalList: List<PersonalScheduleData>
) {
    val time = remember { mutableStateOf(Calendar.getInstance()) }
    val date = time.value

    // 시간 세팅
    date.set(Calendar.YEAR, currentYear)
    date.set(Calendar.MONTH, currentMonth - 1)
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
                    val isSelected = selectedDate == resultDay
                    val dayOfWeek = (day + 1) % 7
                    val tempWorkInfo = tempWorkList.find { it.first == resultDay }

                    val tempResultDay = if (resultDay < 10) "0${resultDay}" else "${resultDay}"
                    val workInfo = monthlyWorkList.find {
                        it.workDate.substring(
                            8,
                            10
                        ) == tempResultDay
                    }?.workType

                    val tempEventCnt = monthlyPersonalList.filter {
                        it.date.substring(8, 10) == tempResultDay
                    }.flatMap {
                        it.eventList
                    }.size

                    val eventCnt = when (tempEventCnt) {
                        null -> ""
                        0 -> ""
                        1 -> "·"
                        2 -> "··"
                        else -> "···"
                    }


                    if (resultDay in 1..thisMonthDayMax) {
                        // 달력 날짜 범위 내
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(70.dp)
                                .clickable {
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
                                if (tempWorkInfo?.second != null) Labels2(tempWorkInfo.second) else if (workInfo != null) Labels2(
                                    workInfo
                                ) else Labels(text = "")
                                Text(
                                    text = eventCnt,
                                    color = Gray600,
                                    style = Typography.labelLarge
                                )
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
//                                Labels(text = "NIGHT")
//                                Text(text = "·")
                            }
                        }
                    }
                }
            }
        }
    }
}