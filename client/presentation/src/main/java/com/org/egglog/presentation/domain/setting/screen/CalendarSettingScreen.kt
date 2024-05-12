package com.org.egglog.presentation.domain.setting.screen

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.domain.setting.model.CalendarGroup
import com.org.egglog.presentation.component.atoms.buttons.SettingSpaceBetweenButton
import com.org.egglog.presentation.component.atoms.icons.Icon
import com.org.egglog.presentation.component.atoms.toggle.Toggle
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.component.molecules.swiper.CalendarSettingSwiper
import com.org.egglog.presentation.domain.setting.viewmodel.CalendarSettingSideEffect
import com.org.egglog.presentation.domain.setting.viewmodel.CalendarSettingViewModel
import com.org.egglog.presentation.theme.*
import com.org.egglog.presentation.utils.ArrowRight
import com.org.egglog.presentation.utils.Sync
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun CalendarSettingScreen(
    viewModel: CalendarSettingViewModel = hiltViewModel(),
    onNavigationToCalendarAddScreen: () -> Unit
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CalendarSettingSideEffect.Toast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }

    CalendarSettingScreen(
        calendarGroupList = state.calendarGroupList,
        getCalendarListInit = viewModel::getCalendarListInit,
        onNavigationToCalendarAddScreen = onNavigationToCalendarAddScreen,
        onClickToggle = viewModel::onClickToggle,
        onClickDelete = viewModel::onClickDelete,
        onClickSync = viewModel::onClickSync,
        syncEnabled = state.syncEnabled,
        deleteEnabled = state.deleteEnabled,
        addEnabled = state.addEnabled
    )
}

@Composable
fun CalendarSettingScreen(
    calendarGroupList: List<CalendarGroup>?,
    getCalendarListInit: () -> Unit,
    onNavigationToCalendarAddScreen: () -> Unit,
    onClickToggle: (Long) -> Unit,
    onClickDelete: (Long) -> Unit,
    onClickSync: () -> Unit,
    syncEnabled: Boolean,
    deleteEnabled: Boolean,
    addEnabled: Boolean
) {
    val context = LocalContext.current
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val currentTime = LocalTime.now().format(formatter)

    LaunchedEffect(deleteEnabled, addEnabled) {
        getCalendarListInit()
    }

    Surface {
        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
        ) {
            BasicHeader(
                title = "캘린더 설정",
                hasTitle = true,
                hasArrow = true,
                hasProgressBar = true,
                onClickBack = { (context as? Activity)?.onBackPressed() },
                onClickLink = { },
                onClickMenus = { },
                selectedOption = null
            )
            Column(
                Modifier
                    .padding(horizontal = 8.widthPercent(context).dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                SettingSpaceBetweenButton(text = "캘린더 추가", onClick = onNavigationToCalendarAddScreen) {
                    Icon(imageVector = ArrowRight, modifier = Modifier.size(24.widthPercent(context).dp))
                }

                calendarGroupList?.map {calendarGroup ->
                    CalendarSettingSwiper(onDelete = { onClickDelete(calendarGroup.calendarGroupId) }, deleteEnabled = deleteEnabled) {
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                            Text(text = "${if(calendarGroup.isBasic) "[기본]" else "[구독]"} ${calendarGroup.alias}", style = Typography.bodyMedium)
                            Toggle(checked = calendarGroup.isEnabled, onCheckedChange = {
                                onClickToggle(calendarGroup.calendarGroupId)
                            })
                        }
                    }
                    HorizontalDivider(color = Gray200)
                }

                SettingSpaceBetweenButton(text = "캘린더 동기화", onClick = { onClickSync() }, enabled = syncEnabled) {
                    Row(Modifier, Arrangement.Center, Alignment.CenterVertically) {
                        Icon(imageVector = Sync, modifier = Modifier.size(18.widthPercent(context).dp))
                        Spacer(modifier = Modifier.padding(2.widthPercent(context).dp))
                        Text(text = "$currentDate $currentTime", style =  Typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun CalendarSettingScreenPreview() {
    ClientTheme {
        CalendarSettingScreen(onNavigationToCalendarAddScreen = {})
    }
}