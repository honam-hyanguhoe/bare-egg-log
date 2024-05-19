package com.org.egglog.presentation.domain.myCalendar.screen

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.Dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.org.egglog.domain.myCalendar.model.EventListData
import com.org.egglog.domain.myCalendar.model.PersonalScheduleData
import com.org.egglog.domain.myCalendar.model.WorkListData
import com.org.egglog.domain.myCalendar.model.WorkType
import com.org.egglog.presentation.R
import com.org.egglog.presentation.component.atoms.buttons.BigButton
import com.org.egglog.presentation.component.atoms.buttons.CustomIconButton
import com.org.egglog.presentation.component.atoms.buttons.FloatingButton
import com.org.egglog.presentation.component.atoms.buttons.HalfBigButton
import com.org.egglog.presentation.component.atoms.dialogs.BottomSheet
import com.org.egglog.presentation.component.atoms.imageLoader.LocalImageLoader
import com.org.egglog.presentation.component.atoms.inputs.SingleInput
import com.org.egglog.presentation.component.atoms.labels.Labels
import com.org.egglog.presentation.component.atoms.labels.Labels2
import com.org.egglog.presentation.component.atoms.toggle.Toggle
import com.org.egglog.presentation.component.atoms.wheelPicker.DateTimePicker
import com.org.egglog.presentation.component.atoms.wheelPicker.TimePicker
import com.org.egglog.presentation.component.molecules.bottomNavigator.BottomNavigator
import com.org.egglog.presentation.component.molecules.cards.BigScheduleCard
import com.org.egglog.presentation.component.molecules.cards.SmallScheduleCard
import com.org.egglog.presentation.component.molecules.cards.SmallScheduleCard2
import com.org.egglog.presentation.component.organisms.calendars.MonthlyCalendar
import com.org.egglog.presentation.component.organisms.dialogs.SheetContent
import com.org.egglog.presentation.domain.community.activity.CommunityActivity
import com.org.egglog.presentation.domain.group.activity.GroupActivity
import com.org.egglog.presentation.domain.main.activity.MainActivity
import com.org.egglog.presentation.domain.myCalendar.viewmodel.MyCalendarSideEffect
import com.org.egglog.presentation.domain.myCalendar.viewmodel.MyCalendarViewModel
import com.org.egglog.presentation.domain.setting.activity.SettingActivity
import com.org.egglog.presentation.domain.setting.navigation.SettingRoute
import com.org.egglog.presentation.theme.Black
import com.org.egglog.presentation.theme.Error200
import com.org.egglog.presentation.theme.Gray300
import com.org.egglog.presentation.theme.Gray400
import com.org.egglog.presentation.theme.Gray500
import com.org.egglog.presentation.theme.NaturalBlack
import com.org.egglog.presentation.theme.NaturalWhite
import com.org.egglog.presentation.theme.Typography
import com.org.egglog.presentation.theme.Warning200
import com.org.egglog.presentation.theme.Warning300
import com.org.egglog.presentation.theme.White
import com.org.egglog.presentation.utils.Close
import com.org.egglog.presentation.utils.Settings
import com.org.egglog.presentation.utils.heightPercent
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Calendar

@Composable
fun MyCalendarScreen(
    viewModel: MyCalendarViewModel = hiltViewModel(),
    onNavigateToExcelScreen: (Int, Int) -> Unit,
    onNavigateToCalendarSettingScreen: () -> Unit,
    onNavigateToWorkSettingScreen: () -> Unit
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current


    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            MyCalendarSideEffect.NavigateToCommunityActivity -> {
                context.startActivity(
                    Intent(
                        context, CommunityActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }

            MyCalendarSideEffect.NavigateToGroupActivity -> {
                context.startActivity(
                    Intent(
                        context, GroupActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }

            MyCalendarSideEffect.NavigateToMainActivity -> {
                context.startActivity(
                    Intent(
                        context, MainActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }

            MyCalendarSideEffect.NavigateToSettingActivity -> {
                context.startActivity(
                    Intent(
                        context, SettingActivity::class.java
                    ).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                )
            }

            is MyCalendarSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.message,
                Toast.LENGTH_SHORT
            ).show()

            MyCalendarSideEffect.NavigateToCalendarSettingScreen -> {
                onNavigateToCalendarSettingScreen()
            }
        }
    }

    val isLoading by viewModel.isLoading.collectAsState()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)

    MyCalendarScreen(
        swipeRefreshState = swipeRefreshState,
        refreshSomething = viewModel::refreshSomething,
        selectedIdx = state.selectedIdx,
        onClickExcelSync = onNavigateToExcelScreen,
        onSelectedIdx = viewModel::onSelectedIdx,
        scheduleTitle = state.scheduleTitle,
        scheduleContent = state.scheduleContent,
        onChangeScheduleTitle = viewModel::onChangeScheduleTitle,
        onChangeScheduleContent = viewModel::onChangeScheduleContent,
        onChangeStartTime = viewModel::onChangeStartTime,
        onChangeEndTime = viewModel::onChangeEndTime,
        onClickCalendarSetting = viewModel::onClickCalendarSetting,
        currentYear = state.currentYear,
        currentMonth = state.currentMonth,
        selectedDate = state.selectedDate,
        onPrevMonthClick = viewModel::onPrevMonthClick,
        onNextMonthClick = viewModel::onNextMonthClick,
        onDateClicked = viewModel::onDateClicked,
        workTypeList = state.workTypeList,
        monthlyWorkList = state.monthlyWorkList,
        currentWorkData = state.currentWorkData,
        monthlyPersonalList = state.monthlyPersonalList,
        currentPersonalData = state.currentPersonalData,
        onSubmitPersonalSchedule = viewModel::onSubmitPersonalSchedule,
        onWorkLabelClick = viewModel::onWorkLabelClick,
        tempWorkList = state.tempWorkList,
        onCancelWorkSchedule = viewModel::onCancelWorkSchedule,
        onSubmitWorkSchedule = viewModel::onSubmitWorkSchedule,
        onNavigateToWorkSettingScreen = onNavigateToWorkSettingScreen,
        onDeletePersonalSchedule = viewModel::onDeletePersonalSchedule,
        onClickModify = viewModel::onClickModify,
        isModifyState = state.isModifyState,
        currentEventId = state.currentEventId,
        onModifyPersonalSchedule = viewModel::onModifyPersonalSchedule,
        getWorkTypeList = viewModel::getWorkTypeList
    )

}

@Composable
fun MyCalendarScreen(
    swipeRefreshState: SwipeRefreshState,
    refreshSomething: () -> Unit,
    selectedIdx: Int,
    onClickExcelSync: (Int, Int) -> Unit,
    onSelectedIdx: (Int) -> Unit,
    scheduleTitle: String,
    scheduleContent: String,
    onChangeScheduleTitle: (String) -> Unit,
    onChangeScheduleContent: (String) -> Unit,
    onChangeStartTime: (LocalDateTime) -> Unit,
    onChangeEndTime: (LocalDateTime) -> Unit,
    onClickCalendarSetting: () -> Unit,
    currentYear: Int,
    currentMonth: Int,
    selectedDate: Int,
    onPrevMonthClick: () -> Unit,
    onNextMonthClick: () -> Unit,
    onDateClicked: (Int) -> Unit,
    workTypeList: List<WorkType>,
    monthlyWorkList: List<WorkListData>,
    currentWorkData: WorkType? = null,
    monthlyPersonalList: List<PersonalScheduleData>,
    currentPersonalData: List<EventListData>? = listOf(),
    onSubmitPersonalSchedule: () -> Unit,
    onWorkLabelClick: (WorkType) -> Unit,
    tempWorkList: List<Pair<Int, WorkType?>>,
    onCancelWorkSchedule: () -> Unit,
    onSubmitWorkSchedule: () -> Unit,
    onNavigateToWorkSettingScreen: () -> Unit,
    onDeletePersonalSchedule: (Int) -> Unit,
    onClickModify: (Int) -> Unit,
    isModifyState: Boolean,
    currentEventId: Int,
    onModifyPersonalSchedule: (Int) -> Unit,
    getWorkTypeList: () -> Unit
) {

    val context = LocalContext.current

    // bottom sheet를 위한 값
    val isWorkBottomSheet = remember { mutableStateOf(false) }
    val isPersonalBottomSheet = remember { mutableStateOf(false) }

    val (isVisible, setVisible) = remember { mutableStateOf(false) }

    Surface(Modifier.systemBarsPadding().background(Black)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(NaturalWhite),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = refreshSomething,
                indicator = { state, refreshTrigger ->
                    SwipeRefreshIndicator(
                        state = state,
                        refreshTriggerDistance = refreshTrigger,
                        backgroundColor = NaturalBlack,
                        contentColor = NaturalWhite
                    )
                }
            ) {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                        .padding(
                            start = 10.widthPercent(context).dp,
                            top = 10.heightPercent(context).dp,
                            end = 10.widthPercent(context).dp
                        )
                ) {

                    item {
                        MonthlyCalendar(
                            currentYear = currentYear,
                            currentMonth = currentMonth,
                            onDateClicked = onDateClicked,
                            onPrevMonthClick = onPrevMonthClick,
                            onNextMonthClick = onNextMonthClick,
                            selectedDate = selectedDate,
                            tempWorkList = tempWorkList,
                            monthlyWorkList = monthlyWorkList,
                            monthlyPersonalList = monthlyPersonalList
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        ScheduleListHeader(currentYear, currentMonth, selectedDate, onClickExcelSync)

                        Spacer(modifier = Modifier.height(10.dp))

                        ScheduleList(
                            currentWorkData,
                            currentPersonalData,
                            onDeletePersonalSchedule,
                            isPersonalBottomSheet,
                            onClickModify
                        )

                    }

                }
            }

            BottomNavigator(selectedItem = selectedIdx, onItemSelected = onSelectedIdx)
        }

        FloatingButton(
            onClick = { setVisible(!isVisible) },
            onWorkClick = {
                isWorkBottomSheet.value = true
                getWorkTypeList()
                setVisible(!isVisible)
            },
            onPersonalClick = {
                isPersonalBottomSheet.value = true
                setVisible(!isVisible)
            },
            onSettingClick = {
                onClickCalendarSetting()
                setVisible(!isVisible)
            },
            horizontalPadding = 10.dp,
            verticalPadding = 90.heightPercent(context).dp,
            isVisible = isVisible
        )

        if (isWorkBottomSheet.value) {
            InteractiveBottomSheet(
                340.heightPercent(context).dp,
                10.dp,
                workTypeList,
                isWorkBottomSheet,
                onWorkLabelClick,
                onCancelWorkSchedule,
                onSubmitWorkSchedule,
                onNavigateToWorkSettingScreen = onNavigateToWorkSettingScreen
            )
        }

        if (isPersonalBottomSheet.value) {
            BottomSheet(
                height = 650.heightPercent(context).dp,
                showBottomSheet = isPersonalBottomSheet.value,
                onDismiss = {
                    isPersonalBottomSheet.value = false
                    onChangeScheduleTitle("")
                    onChangeScheduleContent("")
                    onChangeStartTime(LocalDateTime.now())
                    onChangeEndTime(LocalDateTime.now())
                }) {
                PersonalScheduleForm(
                    scheduleTitle,
                    scheduleContent,
                    onChangeScheduleTitle = onChangeScheduleTitle,
                    onChangeScheduleContent = onChangeScheduleContent,
                    onChangeStartTime = onChangeStartTime,
                    onChangeEndTime = onChangeEndTime,
                    isPersonalBottomSheet = isPersonalBottomSheet,
                    onSubmitPersonalSchedule = onSubmitPersonalSchedule,
                    isModifyState = isModifyState,
                    currentEventId = currentEventId,
                    onModifyPersonalSchedule = onModifyPersonalSchedule
                )
            }
        }
    }
}

@Composable
fun ScheduleListHeader(
    currentYear: Int,
    currentMonth: Int,
    selectedDate: Int,
    onClickExcelSync: (Int,Int) -> Unit,
) {
    val context = LocalContext.current

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${currentMonth}월 ${selectedDate}일",
                color = NaturalBlack,
                style = Typography.bodyLarge
            )
            Spacer(modifier = Modifier.width(10.dp))
//            Toggle(checked = true, onCheckedChange = {})
        }

        Row(
            Modifier.clickable {
                onClickExcelSync(currentYear, currentMonth)
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "근무 일정 동기화", color = Gray500, style = Typography.displayLarge)
            Spacer(modifier = Modifier.width(6.dp))
            Box(modifier = Modifier.background(Color.Transparent, CircleShape)) {
                LocalImageLoader(
                    imageUrl = R.drawable.excel,
                    modifier = Modifier.size(24.widthPercent(context).dp)
                )
            }
        }
    }
}

// 일정 카드 리스트
@Composable
fun ScheduleList(
    currentWorkData: WorkType? = null,
    currentPersonalData: List<EventListData>? = listOf(),
    onDeletePersonalSchedule: (Int) -> Unit,
    isPersonalBottomSheet: MutableState<Boolean>,
    onClickModify: (Int) -> Unit
) {
    Column() {
        if (currentWorkData != null) {
            SmallScheduleCard2(workType = currentWorkData)
        }


        currentPersonalData?.forEach { personalData ->
            BigScheduleCard(
                personalData,
                onClickDelete = { onDeletePersonalSchedule(personalData.eventId) },
                onClickModify = {
                    isPersonalBottomSheet.value = true
                    onClickModify(personalData.eventId)
                })
        }

        if(currentWorkData == null && currentPersonalData == null) {
            Box(
                modifier = Modifier.padding(vertical = 20.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "등록된 일정이 없습니다", color = NaturalBlack)
            }
        }
    }
}

// 개인 일정 입력 폼
@Composable
fun PersonalScheduleForm(
    scheduleTitle: String = "",
    scheduleContent: String = "",
    onChangeScheduleTitle: (String) -> Unit,
    onChangeScheduleContent: (String) -> Unit,
    onChangeStartTime: (LocalDateTime) -> Unit,
    onChangeEndTime: (LocalDateTime) -> Unit,
    isPersonalBottomSheet: MutableState<Boolean>,
    onSubmitPersonalSchedule: () -> Unit,
    isModifyState: Boolean,
    currentEventId: Int,
    onModifyPersonalSchedule: (Int) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center
    ) {
        // 제목
        Text(text = "제목", style = Typography.displayLarge)
        Spacer(modifier = Modifier.height(12.heightPercent(LocalContext.current).dp))
        SingleInput(
            modifier = Modifier.width(320.widthPercent(LocalContext.current).dp),
            text = scheduleTitle,
            placeholder = "제목 입력",
            onValueChange = { scheduleTitle -> onChangeScheduleTitle(scheduleTitle) },
            focusManager = focusManager
        )
        Spacer(modifier = Modifier.height(24.heightPercent(LocalContext.current).dp))

        // 내용
        Text(text = "내용", style = Typography.displayLarge)
        Spacer(modifier = Modifier.height(12.heightPercent(LocalContext.current).dp))
        SingleInput(
            modifier = Modifier.width(320.widthPercent(LocalContext.current).dp),
            text = scheduleContent,
            placeholder = "내용을 입력해주세요",
            onValueChange = { scheduleContent -> onChangeScheduleContent(scheduleContent) },
            focusManager = focusManager,
        )
        Spacer(modifier = Modifier.height(24.heightPercent(LocalContext.current).dp))

        // 시작 시간
        Text(text = "시작 시간", style = Typography.displayLarge)
        DateTimePicker { time -> onChangeStartTime(time) }
        Spacer(modifier = Modifier.height(24.heightPercent(LocalContext.current).dp))

        // 종료 시간
        Text(text = "종료 시간", style = Typography.displayLarge)
        DateTimePicker { time -> onChangeEndTime(time) }
        Spacer(modifier = Modifier.height(24.heightPercent(LocalContext.current).dp))

        BigButton(
            colors = ButtonColors(
                contentColor = NaturalWhite,
                containerColor = Warning300,
                disabledContainerColor = Gray300,
                disabledContentColor = NaturalWhite
            ), onClick = {
                if (isModifyState) {
                    onModifyPersonalSchedule(currentEventId)
                } else {
                    onSubmitPersonalSchedule()
                }
                isPersonalBottomSheet.value = false
            },
            enabled = (scheduleTitle != "" && scheduleContent != "") // title과 content 값이 없으면 disabled
        ) {
            Text(
                style = Typography.bodyLarge,
                color = NaturalWhite,
                text = if (isModifyState) "수정 하기" else "추가 하기"
            )
        }
    }
}

// 근무 입력 bottom sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveBottomSheet(
    height: Dp,
    padding: Dp,
    workTypeList: List<WorkType>,
    isWorkBottomSheet: MutableState<Boolean>,
    onWorkLabelClick: (WorkType) -> Unit,
    onCancelWorkSchedule: () -> Unit,
    onSubmitWorkSchedule: () -> Unit,
    onNavigateToWorkSettingScreen: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.PartiallyExpanded,
        skipHiddenState = false
    );


    val iBottomSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    BottomSheetScaffold(
        sheetContent = {
            WorkScheduleForm(
                workTypeList = workTypeList,
                isWorkBottomSheet = isWorkBottomSheet,
                onWorkLabelClick = onWorkLabelClick,
                onCancelWorkSchedule = onCancelWorkSchedule,
                onSubmitWorkSchedule = onSubmitWorkSchedule,
                onNavigateToWorkSettingScreen = onNavigateToWorkSettingScreen
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
        scaffoldState = iBottomSheetState,
        sheetPeekHeight = height,
        sheetContainerColor = White,
        sheetContentColor = NaturalBlack,
        containerColor = White,
        contentColor = NaturalBlack,
        sheetSwipeEnabled = false,
    ) {
    }
}

// 근무 추가 입력 폼
@Composable
fun WorkScheduleForm(
    workTypeList: List<WorkType>,
    isWorkBottomSheet: MutableState<Boolean>,
    onWorkLabelClick: (WorkType) -> Unit,
    onCancelWorkSchedule: () -> Unit,
    onSubmitWorkSchedule: () -> Unit,
    onNavigateToWorkSettingScreen: () -> Unit
) {
    val context = LocalContext.current

    Column(
        Modifier
            .padding(horizontal = 16.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center
    ) {
        // 근무 일정 추가 content
        Column(modifier = Modifier.height(230.dp)) {
            // 근무 일정 추가 Header
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier) {
                    Text(text = "근무 일정 추가", color = NaturalBlack)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "근무 일정을 추가하세요.\nNone을 클릭하면 입력된 근무를 삭제할 수 있습니다.",
                        color = NaturalBlack,
                        style = Typography.labelMedium
                    )
                }

                Row(
                    Modifier.clickable {
                        onNavigateToWorkSettingScreen()
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomIconButton(
                        size = 14.widthPercent(context).dp,
                        imageVector = Settings,
                        color = Gray400,
                        onClick = { },
                        enabled = false
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "근무 설정", color = Gray400, style = Typography.labelLarge)
                }

            }

            Spacer(modifier = Modifier.height(20.dp))

            val groupedTypeList = workTypeList.chunked(4)

            groupedTypeList.forEach { rowList ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowList.forEach { type ->
                        Labels2(type, size = "big", onClick = {
                            // TODO 선택된 날짜 -> 다음 날짜로 변경
                            onWorkLabelClick(type)
                        })
                    }
                }
            }
        }

        // 근무 일정 추가 buttons
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BigButton(
                modifier = Modifier.width(155.widthPercent(context).dp),
                colors = ButtonColors(
                    contentColor = NaturalWhite,
                    containerColor = Gray300,
                    disabledContainerColor = Gray300,
                    disabledContentColor = NaturalWhite
                ), onClick = {
                    isWorkBottomSheet.value = false
                    onCancelWorkSchedule()
                }) {
                Text(text = "취소", style = Typography.bodyLarge)
            }

            BigButton(
                modifier = Modifier.width(155.widthPercent(context).dp),
                colors = ButtonColors(
                    contentColor = NaturalWhite,
                    containerColor = Warning300,
                    disabledContainerColor = Gray300,
                    disabledContentColor = NaturalWhite
                ), onClick = {
                    // TODO : 일정 추가 요청 보내기
                    onSubmitWorkSchedule()
                    isWorkBottomSheet.value = false
                }) {
                Text(text = "완료", style = Typography.bodyLarge)
            }
        }
    }
}