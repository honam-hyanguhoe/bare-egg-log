package com.org.egglog.presentation.domain.setting.viewmodel

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.setting.model.Notification
import com.org.egglog.domain.setting.model.NotificationListParam
import com.org.egglog.domain.setting.model.NotificationParam
import com.org.egglog.domain.setting.model.NotificationType
import com.org.egglog.domain.setting.usecase.GetNotificationListUseCase
import com.org.egglog.domain.setting.usecase.UpdateNotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.annotation.concurrent.Immutable
import javax.inject.Inject

@HiltViewModel
class NotificationSettingViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserStoreUseCase: GetUserStoreUseCase,
    private val updateNotificationUseCase: UpdateNotificationUseCase,
    private val getNotificationListUseCase: GetNotificationListUseCase
): ViewModel(), ContainerHost<NotificationSettingState, NotificationSettingSideEffect>{
    override val container: Container<NotificationSettingState, NotificationSettingSideEffect> = container(
        initialState = NotificationSettingState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(NotificationSettingSideEffect.Toast(message = throwable.message.orEmpty()))
                    reduce { state.copy(toggleEnabled = true) }
                }
            }
        }
    )

    fun getNotificationInit() = intent {
        val tokens = getTokenUseCase()
        val list = getNotificationListUseCase("Bearer ${tokens.first.orEmpty()}").getOrThrow() ?: emptyList()
        for(notification in list) {
            if (!notification.status) {
                reduce { state.copy(totalStatus = false) }
                break
            } else {
                reduce { state.copy(totalStatus = true) }
            }
        }
        reduce {
            state.copy(notificationList = list)
        }
    }

    fun onToggleAllChange() = intent {
        reduce { state.copy(toggleEnabled = false) }
        val tokens = getTokenUseCase()
        updateNotificationUseCase(
            accessToken = "Bearer ${tokens.first.orEmpty()}",
            notificationListParam = NotificationListParam(
                state.notificationList?.map { NotificationParam(it.notificationId, !state.totalStatus) } ?: emptyList()
            )).getOrThrow()
        reduce { state.copy(toggleEnabled = true) }
    }

    fun onToggleChange(notificationId: Long, enabled: Boolean) = intent {
        reduce { state.copy(toggleEnabled = false) }
        val tokens = getTokenUseCase()
        updateNotificationUseCase(accessToken = "Bearer ${tokens.first.orEmpty()}", notificationListParam = NotificationListParam(listOf(NotificationParam(notificationId = notificationId, status = enabled)))).getOrThrow()
        reduce { state.copy(toggleEnabled = true) }
    }
}

@Immutable
data class NotificationSettingState(
    val toggleEnabled: Boolean = true,
    val totalStatus: Boolean = true,
    val notificationList: List<Notification>? = emptyList()
)

sealed interface NotificationSettingSideEffect {
    class Toast(val message: String): NotificationSettingSideEffect
}