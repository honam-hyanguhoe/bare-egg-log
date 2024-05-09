package com.org.egglog.presentation.domain.setting.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.org.egglog.domain.auth.model.AddUserParam
import com.org.egglog.domain.auth.model.HospitalParam
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.model.UserHospital
import com.org.egglog.domain.auth.usecase.DeleteTokenUseCase
import com.org.egglog.domain.auth.usecase.DeleteUserStoreUseCase
import com.org.egglog.domain.auth.usecase.GetAllHospitalUseCase
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.auth.usecase.PostLogoutUseCase
import com.org.egglog.domain.auth.usecase.SetUserStoreUseCase
import com.org.egglog.domain.auth.usecase.UpdateUserJoinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
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
class SettingViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val postLogoutUseCase: PostLogoutUseCase,
    private val deleteTokenUseCase: DeleteTokenUseCase,
    private val deleteUserStoreUseCase: DeleteUserStoreUseCase,
    private val getUserStoreUseCase: GetUserStoreUseCase
): ViewModel(), ContainerHost<SettingState, SettingSideEffect>{
    override val container: Container<SettingState, SettingSideEffect> = container(
        initialState = SettingState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(SettingSideEffect.Toast(message = throwable.message.orEmpty()))
                }
            }
        }
    )

    fun onClickLogout() = intent {
        val tokens = getTokenUseCase()
        postLogoutUseCase(tokens.first ?: "")
        deleteTokenUseCase()
        deleteUserStoreUseCase()
        postSideEffect(SettingSideEffect.NavigateToLoginActivity)
    }
}


@Immutable
data class SettingState(
    val user: UserDetail? = null,
)

sealed interface SettingSideEffect {
    class Toast(val message: String): SettingSideEffect
    data object NavigateToMainActivity: SettingSideEffect
    data object NavigateToLoginActivity: SettingSideEffect
}