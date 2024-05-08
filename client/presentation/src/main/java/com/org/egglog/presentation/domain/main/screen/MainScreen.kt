package com.org.egglog.presentation.domain.main.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.presentation.component.molecules.radioButtons.DayRadioButton
import com.org.egglog.presentation.component.organisms.calendars.WeeklyCalendar
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyDataSource
import com.org.egglog.presentation.component.organisms.calendars.weeklyData.WeeklyUiModel
import com.org.egglog.presentation.component.organisms.webView.ContentWebView
import com.org.egglog.presentation.domain.main.viewModel.WorkViewModel
import com.org.egglog.presentation.theme.Gray100
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import java.time.LocalDate


@Composable
fun MainScreen(
    workViewModel: WorkViewModel = hiltViewModel()
) {
    val workState = workViewModel.collectAsState().value

    MainScreen(
        startDate = workState.startDate,
        labels = workState.labels,
        calendarUiModel = workState.currentWeekDays,
        onPrevClick = workViewModel::onPrevClick,
        onNextClick = workViewModel::onNextClick
    )
}


@Composable
private fun MainScreen(
    startDate: LocalDate,
    calendarUiModel: WeeklyUiModel,
    onPrevClick: (LocalDate) -> Unit,
    onNextClick: (LocalDate) -> Unit,
    labels: List<String>
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = NaturalWhite)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Box(
            modifier = Modifier
                .width(340.widthPercent(context).dp)
                .height(185.heightPercent(context).dp)
                .border(
                    1.dp, NaturalBlack,
                    RoundedCornerShape(15.dp)
                )
        ) {
            Text(text = "으아. 집 갈래요", style = Typography.displayLarge)
        }
        Spacer(modifier = Modifier.height(30.dp))
        DutyCard(
            startDate = startDate,
            calendarUiModel = calendarUiModel,
            onPrevClick = { onPrevClick(startDate) },
            onNextClick = onNextClick,
            labels = labels
        )
        Spacer(modifier = Modifier.height(30.dp))
        RemainCard()
        Spacer(modifier = Modifier.height(30.dp))
        StaticsCard()
    }
}

@Composable
fun DutyCard(
    startDate: LocalDate,
    calendarUiModel: WeeklyUiModel,
    onPrevClick: (LocalDate) -> Unit,
    onNextClick: (LocalDate) -> Unit,
    labels: List<String>
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .width(320.widthPercent(context).dp)
            .background(color = Gray100, shape = RoundedCornerShape(20.dp))
            .padding(8.dp, 15.dp, 8.dp, 25.dp)
    ) {
        WeeklyCalendar(
            type = "main",
            startDate = startDate,
            calendarUiModel = calendarUiModel,
            labels = labels,
            onPrevClick = onPrevClick,
            onNextClick = onNextClick,
            width = 320,
            backgroundColor = Gray100
        )
    }
}

@Composable
fun RemainCard() {
    val context = LocalContext.current
    // radio
    val radioList = arrayListOf("Week", "Month")
    val selected = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .width(320.widthPercent(context).dp)
            .background(color = Gray100, shape = RoundedCornerShape(20.dp))
            .padding(8.dp, 15.dp, 8.dp, 25.dp)
    ) {
        Text(
            text = "제 근무는 언제 끝나죠?",
            modifier = Modifier.padding(start = 10.dp),
            style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Box(modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 0.dp, bottom = 0.dp)) {
            DayRadioButton(radioList = radioList, selected = selected)
        }
        ContentWebView(width = 320, height = 350, url = "https://www.egg-log.org/remain")
    }
}

@Composable
fun StaticsCard() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .width(320.widthPercent(context).dp)
            .background(color = Gray100, shape = RoundedCornerShape(20.dp))
            .padding(8.dp, 15.dp, 8.dp, 25.dp)
    ) {
        Text(
            text = "내 근무 통계",
            modifier = Modifier.padding(start = 10.dp),
            style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        ContentWebView(width = 320, height = 350, url = "https://www.egg-log.org/statics")
    }
}

@Preview
@Composable
fun MainScreenPreview() {
//    MainScreen()
}


