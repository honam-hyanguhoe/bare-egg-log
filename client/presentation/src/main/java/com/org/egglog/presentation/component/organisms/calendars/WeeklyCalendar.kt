package com.org.egglog.presentation.component.organisms.calendars

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.org.egglog.presentation.component.atoms.buttons.CustomIconButton
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyDataSource
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyUiModel
import com.org.egglog.presentation.utils.widthPercent
import com.org.egglog.presentation.component.atoms.labels.Labels
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.ArrowLeft
import com.org.egglog.presentation.utils.ArrowRight
import java.time.LocalDate
import java.time.format.DateTimeFormatter


// type = group일 땐 날짜 이동 가능
// type = group이 아닐 땐 날짜 이동 불가능
@Composable
fun WeeklyCalendar(
    type: String,
    startDate: LocalDate,
    calendarUiModel: WeeklyUiModel,
    onDateClick: ((WeeklyUiModel.Date) -> Unit)? = null,
    onPrevClick: (LocalDate) -> Unit,
    onNextClick: (LocalDate) -> Unit,
    labels: List<String>,
    backgroundColor: Color = NaturalWhite,
    width: Int = 320,
    contentsColor: Color = Gray100,
    containerColor: Color = Gray100,
    ) {

    Column(
        Modifier
            .width(width.widthPercent(LocalContext.current).dp)
            .background(color = backgroundColor)
    ) {
        if (type.equals("group")) {
            Row {
                CalendarHeader(calendarUiModel, { onPrevClick(startDate) }, onNextClick)
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        if (type.equals("main")) {
            Column {
                MainCalendarHeader(
                    calendarUiModel, startDate, { onPrevClick(startDate) }, onNextClick
                )
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
        Content(
            type,
            calendarUiModel,
            onDateClick,
            labels = labels,
            backgroundColor = contentsColor,
            containerColor = containerColor
        )
    }
}

@Composable
fun MainCalendarHeader(
    data: WeeklyUiModel,
    startDate: LocalDate,
    onPrevClick: ((LocalDate) -> Unit)? = null,
    onNextClick: ((LocalDate) -> Unit)? = null
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "내 근무 일정",
            modifier = Modifier,
            style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Row(
            modifier = Modifier
        ) {
            CustomIconButton(size = 30.dp,
                imageVector = ArrowLeft,
                color = NaturalBlack,
                onClick = {
                    if (onPrevClick != null) {
                        Log.d("weekly", "startDate ${startDate}")
                        onPrevClick(startDate)
                    }
                })
            CustomIconButton(size = 30.dp,
                imageVector = ArrowRight,
                color = NaturalBlack,
                onClick = { if (onNextClick != null) onNextClick(data.endDate.date) })
        }
    }
}

@Composable
fun CalendarHeader(
    data: WeeklyUiModel,
    onPrevClick: (LocalDate) -> Unit,
    onNextClick: (LocalDate) -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = data.selectedDate.date.format(DateTimeFormatter.ofPattern("yyyy.MM")),
            style = Typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Row() {
            IconButton(onClick = { onPrevClick(data.selectedDate.date) }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous",
                    Modifier.size(20.dp)
                )
            }
            IconButton(onClick = { onNextClick(data.endDate.date) }) {
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
fun Content(
    type: String,
    data: WeeklyUiModel,
    onDateClick: ((WeeklyUiModel.Date) -> Unit)? = null,
    labels: List<String>,
    backgroundColor: Color,
    containerColor: Color = Gray100,
) {
    Log.d("weekly", "data ${data}")
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color = containerColor), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        data.visibleDates.forEachIndexed() { index, date ->
            DateItem(
                type, date, onDateClick, labels[index] ?: "", backgroundColor = backgroundColor
            )
        }
    }
}


@Composable
fun DateItem(
    type: String,
    date: WeeklyUiModel.Date,
    onClick: ((WeeklyUiModel.Date) -> Unit)? = null,
    label: String? = null,
    backgroundColor: Color = NaturalWhite
) {
    Column {
        Card(
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 2.dp)
                .then(
                    if (type == "group") {
                        Modifier.clickable {
                            onClick?.invoke(date)
                        }
                    } else {
                        Modifier
                    }
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (date.isSelected) {
                    if (type == "main") backgroundColor else Warning300
                } else {
                    backgroundColor
                }
            ),
            border = if (date.isToday) {
                BorderStroke(2.dp, Warning300)
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
                    color = when {
                        date.day == "일" -> Color(0xFFF97066)
                        date.isSelected -> NaturalWhite
                        else -> NaturalBlack
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = Typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = date.date.dayOfMonth.toString(),
                    color = if (date.isSelected) {
                        if (type == "main") NaturalBlack else NaturalWhite
                    } else {
                        NaturalBlack
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = Typography.displayMedium
                )
            }
        }
        if (type != "group") {
            Labels(text = label ?: "None")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WCTest() {
    MaterialTheme {
        Column {

            val dataSource = WeeklyDataSource()
            var calendarUiModel2 by remember { mutableStateOf(dataSource.getData(lastSelectedDate = dataSource.today)) }
            val labels: List<String> = listOf("Night", "Day", "휴가", "보건", "Off", "Eve", "Eve")

//            WeeklyCalendar(type = "main", calendarUiModel2, labels = labels, onPrevClick = {}, onNextClick = {})
        }

    }
}