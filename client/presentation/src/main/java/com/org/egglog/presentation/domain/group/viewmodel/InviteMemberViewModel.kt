package com.org.egglog.presentation.domain.group.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.group.usecase.InviteMemberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.annotation.OrbitExperimental
import org.orbitmvi.orbit.syntax.simple.blockingIntent
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.io.InputStream
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class InviteMemberViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val inviteMemberUseCase: InviteMemberUseCase,
) : ViewModel(), ContainerHost<InviteMemberState, InviteMemberSideEffect> {
    override val container: Container<InviteMemberState, InviteMemberSideEffect> =
        container(initialState = InviteMemberState(), buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(InviteMemberSideEffect.Toast(throwable.message.orEmpty()))
                }
            }
        })

    private var accessToken: String ?= ""
    init {
        intent {
            accessToken = "Bearer ${getTokenUseCase().first}"
        }
    }

    fun setInvitationCode(code: String) {
        intent {
            reduce {
                state.copy(invitationCode = code)
            }
        }
    }

    @OptIn(OrbitExperimental::class)
    fun onChangeInvitePassword(value: String) = blockingIntent {
        reduce {
            state.copy(
                invitePassword = value
            )
        }
    }

    fun onJoinGroup() = blockingIntent {
        val result = inviteMemberUseCase(
            accessToken = accessToken ?: "",
            invitationCode = state.invitationCode,
            password = state.invitePassword
        )

        if(result.isSuccess){
            postSideEffect(
                InviteMemberSideEffect.NavigateToGroupListScreen
            )
            postSideEffect(
                InviteMemberSideEffect.Toast("그룹에 가입되었습니다.")
            )
            reduce {
                state.copy(
                    invitePassword = ""
                )
            }
            Log.d("invite", "success")
        }else{
            postSideEffect(InviteMemberSideEffect.Toast("그룹에 가입하지 못했습니다"))
            reduce {
                state.copy(
                    invitePassword = ""
                )
            }
            Log.d("invite", "fail")
        }
    }
}

data class InviteMemberState(
    val invitationCode: String = "",
    val invitePassword : String = "",
)
sealed interface InviteMemberSideEffect{
    class Toast(val message: String) : InviteMemberSideEffect

    object NavigateToGroupListScreen : InviteMemberSideEffect
}

