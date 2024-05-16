package com.org.egglog.presentation.domain.myCalendar.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Text
import com.org.egglog.presentation.component.molecules.cards.ExcelCard
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.domain.myCalendar.viewmodel.ExcelListViewModel
import com.org.egglog.presentation.domain.myCalendar.viewmodel.MyCalendarViewModel
import com.org.egglog.presentation.theme.Black
import com.org.egglog.presentation.theme.ClientTheme
import com.org.egglog.presentation.theme.Gray500
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Typography

@Composable
fun ExcelListScreen(
    currentYear: Int,
    currentMonth: Int,
    viewModel: ExcelListViewModel = hiltViewModel(),
    onNavigateToMyCalendarScreen: () -> Unit
) {
    ExcelListScreen(
        currentYear = currentYear,
        currentMonth = currentMonth,
        onNavigateToMyCalendarScreen = onNavigateToMyCalendarScreen
    )
}

@Composable
private fun ExcelListScreen(
    currentYear: Int,
    currentMonth: Int,
    onNavigateToMyCalendarScreen: () -> Unit
) {


    Column(
        Modifier
            .fillMaxSize()
            .background(NaturalWhite)
    ) {
        BasicHeader(
            title = "근무표 선택",
            hasTitle = true,
            hasArrow = true,
            onClickBack = onNavigateToMyCalendarScreen,
            hasProgressBar = true
        )

        Spacer(modifier = Modifier.height(30.dp))

        LazyColumn(Modifier.padding(horizontal = 10.dp)) {
            item {
                Column {
                    Text(
                        text = "동기화 할 근무표를 선택해주세요.",
                        color = NaturalBlack,
                        style = Typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "* 근무표 동기화 시 이전 근무 일지가 사라질 수 있습니다",
                        color = Gray500,
                        style = Typography.displayLarge
                    )
                }
            }

            item {
                Column(Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)) {
                    val excelLists = listOf("2024-05-04", "2024-05-05", "2024-05-07", "2024-05-07", "2024-05-07", "2024-05-07")
                    val excelGroupList = excelLists.chunked(2)

                    excelGroupList.forEach { excelGroup ->
                        Row(
                            Modifier.fillMaxSize().padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            excelGroup.forEachIndexed { index, date ->
                                ExcelCard(color = if ((index+1) % 2 == 0) "white" else "green", date = date, name = "김싸피", onClickCard = {})
                            }
                        }
                    }

                }

            }

        }
    }
}