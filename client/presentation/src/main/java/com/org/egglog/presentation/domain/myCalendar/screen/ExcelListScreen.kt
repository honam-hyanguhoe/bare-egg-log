package com.org.egglog.presentation.domain.myCalendar.screen

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Text
import com.org.egglog.domain.myCalendar.model.ExcelData
import com.org.egglog.presentation.component.atoms.cards.ResultCard
import com.org.egglog.presentation.component.atoms.dialogs.Dialog
import com.org.egglog.presentation.component.molecules.cards.ExcelCard
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.domain.myCalendar.activity.MyCalendarActivity
import com.org.egglog.presentation.domain.myCalendar.viewmodel.ExcelListSideEffect
import com.org.egglog.presentation.domain.myCalendar.viewmodel.ExcelListViewModel
import com.org.egglog.presentation.domain.myCalendar.viewmodel.MyCalendarViewModel
import com.org.egglog.presentation.domain.setting.activity.SettingActivity
import com.org.egglog.presentation.theme.Black
import com.org.egglog.presentation.theme.ClientTheme
import com.org.egglog.presentation.theme.Gray500
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.utils.MessageUtil
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ExcelListScreen(
    currentYear: Int,
    currentMonth: Int,
    viewModel: ExcelListViewModel = hiltViewModel(),
    onNavigateToMyCalendarScreen: () -> Unit
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    viewModel.collectSideEffect {
        sideEffect -> when(sideEffect) {
        is ExcelListSideEffect.Toast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        ExcelListSideEffect.NavigateToCalendarActivity -> {
            context.startActivity(
                Intent(
                    context, MyCalendarActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
            )
        }
    }
    }

    ExcelListScreen(
        currentYear = currentYear,
        currentMonth = currentMonth,
        onNavigateToMyCalendarScreen = onNavigateToMyCalendarScreen,
        excelLists = state.excelList,
        onConfirm = viewModel::onConfirm
    )
}

@Composable
private fun ExcelListScreen(
    currentYear: Int,
    currentMonth: Int,
    onNavigateToMyCalendarScreen: () -> Unit,
    excelLists: List<ExcelData>,
    onConfirm: (Int) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }
    val selectedExcelIndex = remember { mutableStateOf(-1) }

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
                    if(excelLists.isEmpty()) {
                        Box(modifier = Modifier.padding(30.dp)) {
                            ResultCard(message = MessageUtil.NO_LIST_RESULT)
                        }
                    } else {
                        val excelGroupList = excelLists.chunked(2)

                        excelGroupList.forEach { excelGroup ->
                            Row(
                                Modifier
                                    .fillMaxSize()
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                excelGroup.forEachIndexed { index, excelData ->
                                    ExcelCard(
                                        color = if ((index + 1) % 2 == 0) "white" else "green",
                                        date = excelData.date,
                                        name = excelData.userName,
                                        onClickCard = {
                                            selectedExcelIndex.value = index
                                            openDialog.value = true
                                        })
                                }
                            }
                        }
                    }
                }

            }

        }

        if (openDialog.value) {
            Dialog(
                onDismissRequest = {
                    selectedExcelIndex.value = -1
                    openDialog.value = false
                },
                onConfirmation = {
                    onConfirm(selectedExcelIndex.value)
                    openDialog.value = false
                },
                dialogTitle = "동기화 하시겠습니까?",
                dialogText = "동기화 시 이전에 입력한 근무 데이터는 사라집니다."
            )
        }
    }
}