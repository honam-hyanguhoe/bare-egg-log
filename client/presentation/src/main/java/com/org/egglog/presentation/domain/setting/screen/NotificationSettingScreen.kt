package com.org.egglog.presentation.domain.setting.screen

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.org.egglog.domain.setting.model.Notification
import com.org.egglog.domain.setting.model.toBody
import com.org.egglog.domain.setting.model.toTitle
import com.org.egglog.presentation.component.molecules.headers.BasicHeader
import com.org.egglog.presentation.component.molecules.listItems.AlarmListItem
import com.org.egglog.presentation.domain.setting.viewmodel.NotificationSettingSideEffect
import com.org.egglog.presentation.domain.setting.viewmodel.NotificationSettingViewModel
import com.org.egglog.presentation.utils.addFocusCleaner
import com.org.egglog.presentation.utils.widthPercent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun NotificationSettingScreen(
    viewModel: NotificationSettingViewModel = hiltViewModel()
){
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is NotificationSettingSideEffect.Toast -> Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
        }
    }

    NotificationSettingScreen(
        notificationList = state.notificationList,
        getNotificationInit = viewModel::getNotificationInit,
        toggleEnabled = state.toggleEnabled,
        totalStatus = state.totalStatus,
        onToggleChange = viewModel::onToggleChange
    )
}

@Composable
fun NotificationSettingScreen(
    notificationList: List<Notification>?,
    getNotificationInit: () -> Unit,
    toggleEnabled: Boolean,
    totalStatus: Boolean,
    onToggleChange: (Long, Boolean) -> Unit

) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(toggleEnabled) {
        getNotificationInit()
    }

    Surface {
        Column(
            Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .addFocusCleaner(focusManager)
        ) {
            BasicHeader(
                title = "알림 설정",
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
                    .fillMaxWidth()
                    .padding(horizontal = 10.widthPercent(context).dp),
            ) {
                AlarmListItem(title = "전체 알림", body = "전체 알림을 관리합니다.", checked = totalStatus, onCheckedChange = {})
                notificationList?.map {
                    AlarmListItem(title = it.type.toTitle(), body = it.type.toBody(), checked = it.status, onCheckedChange = { enabled -> onToggleChange(it.notificationId, enabled) })
                }
            }
        }
    }
}