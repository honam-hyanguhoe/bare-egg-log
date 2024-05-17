package com.org.egglog.presentation.domain.setting.screen

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.domain.setting.model.Alarm
import com.org.egglog.domain.setting.model.WorkType
import com.org.egglog.presentation.component.atoms.cards.BannerCard
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.component.molecules.tabBar.TabBar
import com.org.egglog.presentation.component.organisms.workSetting.AlarmManager
import com.org.egglog.presentation.component.organisms.workSetting.WorkManager
import com.org.egglog.presentation.domain.setting.viewmodel.WorkSettingSideEffect
import com.org.egglog.presentation.domain.setting.viewmodel.WorkSettingViewModel
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.addFocusCleaner
import com.org.egglog.presentation.utils.heightPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalTime

@Composable
fun WorkSettingScreen(
    viewModel: WorkSettingViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is WorkSettingSideEffect.Toast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }

    WorkSettingScreen(
        showCreateBottomSheet = state.showCreateBottomSheet,
        setShowCreateBottomSheet = viewModel::setShowCreateBottomSheet,
        showModifyBottomSheet = state.showModifyBottomSheet,
        setShowModifyBottomSheet = viewModel::setShowModifyBottomSheet,
        workTypeList = state.workTypeList,
        getWorkListInit = viewModel::getWorkListInit,
        title = state.title,
        color = state.color,
        onTitleChange = viewModel::onTitleChange,
        onColorChange = viewModel::onColorChange,
        onStartTimeChange = viewModel::onStartTimeChange,
        onEndTimeChange = viewModel::onEndTimeChange,
        onClickAdd = viewModel::onClickAdd,
        onClickModify = viewModel::onClickModify,
        addEnabled = state.addEnabled,
        modifyEnabled = state.modifyEnabled,
        deleteEnabled = state.deleteEnabled,
        onSelected = viewModel::onSelected,
        selectedWorkType = state.selectedWorkType,
        deleteSelectedWorkType = viewModel::deleteSelectedWorkType,

        setAlarm = viewModel::setAlarm,
        alarmList = state.alarmList,
        getAlarmListInit = viewModel::getAlarmListInit,
        onClickToggle = viewModel::onClickToggle,
        toggleEnabled = state.toggleEnabled,
        showModifyBottomSheetAlarm = state.showModifyBottomSheetAlarm,
        modifyEnabledAlarm = state.modifyEnabledAlarm,
        setShowModifyBottomSheetAlarm = viewModel::setShowModifyBottomSheetAlarm,
        setSelectedAlarm = viewModel::setSelectedAlarm,
        deleteSelectedAlarm = viewModel::deleteSelectedAlarm,
        onReplayCntChange = viewModel::onReplayCntChange,
        onReplayTimeChange = viewModel::onReplayTimeChange,
        onClickModifyAlarm = viewModel::onClickModifyAlarm,
        onAlarmTimeChange = viewModel::onAlarmTimeChange
    )
}

@Composable
fun WorkSettingScreen(
    showCreateBottomSheet: Boolean,
    setShowCreateBottomSheet: (Boolean) -> Unit,
    showModifyBottomSheet: Boolean,
    setShowModifyBottomSheet: (Boolean) -> Unit,
    workTypeList: List<WorkType>,
    getWorkListInit: () -> Unit,
    title: String,
    color: String,
    onTitleChange: (String) -> Unit,
    onColorChange: (String) -> Unit,
    onStartTimeChange: (LocalTime) -> Unit,
    onEndTimeChange: (LocalTime) -> Unit,
    onClickAdd: () -> Unit,
    onClickModify: () -> Unit,
    addEnabled: Boolean,
    deleteEnabled: Boolean,
    modifyEnabled: Boolean,
    onSelected: (String, WorkType) -> Unit,
    selectedWorkType: WorkType?,
    deleteSelectedWorkType: () -> Unit,

    setAlarm: () -> Unit,
    alarmList: List<Alarm>,
    getAlarmListInit: () -> Unit,
    onClickToggle: (Alarm) -> Unit,
    toggleEnabled: Boolean,
    showModifyBottomSheetAlarm: Boolean,
    modifyEnabledAlarm: Boolean,
    setShowModifyBottomSheetAlarm: (Boolean) -> Unit,
    setSelectedAlarm: (Alarm) -> Unit,
    deleteSelectedAlarm: () -> Unit,
    onReplayCntChange: (Int) -> Unit,
    onReplayTimeChange: (Int) -> Unit,
    onClickModifyAlarm: () -> Unit,
    onAlarmTimeChange: (LocalTime) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(addEnabled, deleteEnabled, modifyEnabled, toggleEnabled, modifyEnabledAlarm) {
        getWorkListInit()
        getAlarmListInit()
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(NaturalWhite)
            .systemBarsPadding()
            .addFocusCleaner(focusManager)
    ) {
        BasicHeader(
            title = "근무 설정",
            hasTitle = true,
            hasArrow = true,
            hasProgressBar = false,
            onClickBack = { (context as? Activity)?.onBackPressed() },
            onClickLink = { },
            onClickMenus = { },
            selectedOption = null
        )
        Spacer(modifier = Modifier.padding(6.heightPercent(context).dp))
        BannerCard()
        TabBar(titles = listOf("근무", "알람"),
            firstContent = {
                WorkManager(
                    showCreateBottomSheet = showCreateBottomSheet,
                    setShowCreateBottomSheet = setShowCreateBottomSheet,
                    showModifyBottomSheet = showModifyBottomSheet,
                    setShowModifyBottomSheet = setShowModifyBottomSheet,
                    workTypeList = workTypeList,
                    title = title,
                    color = color,
                    onTitleChange = onTitleChange,
                    onColorChange = onColorChange,
                    onStartTimeChange = onStartTimeChange,
                    onEndTimeChange = onEndTimeChange,
                    onClickAdd = onClickAdd,
                    onClickModify = onClickModify,
                    onSelected = onSelected,
                    selectedWorkType = selectedWorkType,
                    addEnabled = addEnabled,
                    deleteSelectedWorkType = deleteSelectedWorkType
                ) },
            secendContent = {
                AlarmManager(
                    setAlarm = setAlarm,
                    alarmList = alarmList,
                    onClickToggle = onClickToggle,
                    showModifyBottomSheetAlarm = showModifyBottomSheetAlarm,
                    modifyEnabledAlarm = modifyEnabledAlarm,
                    setShowModifyBottomSheetAlarm = setShowModifyBottomSheetAlarm,
                    setSelectedAlarm = setSelectedAlarm,
                    deleteSelectedAlarm = deleteSelectedAlarm,
                    onReplayCntChange = onReplayCntChange,
                    onReplayTimeChange = onReplayTimeChange,
                    onClickModifyAlarm = onClickModifyAlarm,
                    onAlarmTimeChange = onAlarmTimeChange
                )
            }
        )
    }
}

@Preview
@Composable
private fun MySettingScreenPreview() {
    ClientTheme {
        MySettingScreen()
    }
}