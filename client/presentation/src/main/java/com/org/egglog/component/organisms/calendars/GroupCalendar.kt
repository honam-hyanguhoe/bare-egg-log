package com.org.egglog.component.organisms.calendars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.org.egglog.component.atoms.labels.Labels
import com.org.egglog.theme.*
import java.util.Calendar

@Composable
fun GroupCalenar(
        currentYear: MutableState<Int>,
        currentMonth: MutableState<Int>,
        workList: Map<String, Map<String, String>>
) {


    Column(Modifier
            .fillMaxWidth()
    ) {
        GroupCalendarHeader()
        Spacer(modifier = Modifier.height(16.dp))
        GroupDayList(currentYear, currentMonth, workList)
    }
}


@Composable
fun GroupCalendarHeader() {
    val nameList = listOf("아", "일", "월", "화", "수", "목", "금", "토")

    Row(Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
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
                        color = if(it.equals("일")) Error600 else if(it.equals("토")) Blue700 else if(it.equals("아"))  NaturalWhite else NaturalBlack
                )
            }
        }

    }
}

@Composable
fun GroupDayList(
        currentYear: MutableState<Int>,
        currentMonth: MutableState<Int>,
        workList: Map<String, Map<String, String>>,
) {
    val time = remember { mutableStateOf(Calendar.getInstance()) }
    val date = time.value
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
                Column(modifier = Modifier
                        .padding(4.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                        )
                {
                    Text(
                            text = "",
//                            Modifier.border(1.dp, NaturalBlack),
                            style = Typography.displayMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    workList.entries.forEachIndexed { index, (user, workMap) ->
                        val textStyle = if (index == 0) {
                            Typography.displayLarge
                        } else {
                            Typography.displayMedium
                        }
                        Text(user, style = textStyle)
                        Spacer(Modifier.height(8.dp))
                    }
                }

                repeat(7) { day ->
                    val resultDay = week * 7 + day - thisMonthFirstDay + 1

                    val dayOfWeek = (day + 1) % 7

                    val formattedMonth: String =
                        if (currentMonth.value < 10) {
                            "0" + currentMonth.value
                        } else {
                            currentMonth.value.toString()
                        }
                    val formattedDay: String =
                        if (resultDay < 10) {
                            "0" + resultDay
                        } else {
                            resultDay.toString()
                        }

                    val dateString = "${currentYear.value}-${formattedMonth}-${formattedDay}"
                    val workContent = workList.flatMap { it.value.entries }
                            .find { it.key == dateString }
                            ?.value

                    if (resultDay in 1..thisMonthDayMax) {
                        // 달력 날짜 범위 내
                        Box(
                                modifier = Modifier
                                        .weight(1f)
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
                                Spacer(modifier = Modifier.height(10.dp))

                                // 해당 날짜에 근무 내용이 있는 사용자 리스트 가져오기
                                val usersWithWork = workList.filterValues { it.containsKey(dateString) }
                                // 각 사용자별로 근무 내용 표시
                                usersWithWork.forEach { (user, workMap) ->
                                    Labels(text = "${workMap[dateString]}")
                                    Spacer(Modifier.height(8.dp))
                                }

                            }
                        }
                    } else {
                        // 달력 날짜 범위 밖
                        Box(
                                modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp),
                                contentAlignment = Alignment.TopCenter
                        ) {
                            Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}