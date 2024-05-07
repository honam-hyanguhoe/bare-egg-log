package com.org.egglog.presentation.domain.auth.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import com.org.egglog.domain.auth.model.AddUserParam
import com.org.egglog.domain.auth.model.HospitalParam
import com.org.egglog.domain.auth.model.UserHospital
import com.org.egglog.domain.auth.usecase.DeleteTokenUseCase
import com.org.egglog.domain.auth.usecase.DeleteUserStoreUseCase
import com.org.egglog.domain.auth.usecase.GetAllHospitalUseCase
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
class PlusLoginViewModel @Inject constructor(
    private val deleteUserStoreUseCase: DeleteUserStoreUseCase,
    private val deleteTokenUseCase: DeleteTokenUseCase,
    private val setUserStoreUseCase: SetUserStoreUseCase,
    private val updateUserJoinUseCase: UpdateUserJoinUseCase,
    private val getAllHospitalUseCase: GetAllHospitalUseCase
): ViewModel(), ContainerHost<PlusLoginState, PlusLoginSideEffect>{
    override val container: Container<PlusLoginState, PlusLoginSideEffect> = container(
        initialState = PlusLoginState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(PlusLoginSideEffect.Toast(message = throwable.message.orEmpty()))
                }
            }
        }
    )

    @OptIn(OrbitExperimental::class)
    fun onNameChange(name: String) = blockingIntent {
        reduce {
            state.copy(name = name)
        }
    }

    @OptIn(OrbitExperimental::class)
    fun onSearchChange(search: String) = blockingIntent {
        reduce {
            state.copy(search = search)
        }
    }

    @OptIn(OrbitExperimental::class)
    fun onHospitalSelected(hospital: UserHospital) = blockingIntent {
        reduce {
            state.copy(hospital = hospital)
        }
    }

    @OptIn(OrbitExperimental::class)
    fun onEmpNoChange(empNo: String) = blockingIntent {
        reduce {
            state.copy(empNo = empNo)
        }
    }

    fun goLoginActivity() = intent {
        deleteTokenUseCase()
        deleteUserStoreUseCase()
        postSideEffect(PlusLoginSideEffect.NavigateToLoginActivity)
    }

    fun onClickJoin() = intent {
        val fcmToken = try {
            Firebase.messaging.token.await()
        } catch (e: Exception) {
            Log.e("FCM Token", "Error fetching FCM token: ${e.message}", e)
            null
        }
        val result = updateUserJoinUseCase(AddUserParam(userName = state.name, empNo = state.empNo, hospitalId = state.hospital!!.hospitalId, fcmToken = fcmToken)).getOrThrow()
        if(result != null) {
            setUserStoreUseCase(result)
            postSideEffect(PlusLoginSideEffect.NavigateToMainActivity)
        } else {
            postSideEffect(PlusLoginSideEffect.Toast("회원가입에 실패했습니다."))
        }
    }

    fun onClickDone() = intent {
        Log.e("done: ", state.search)
        if(state.search == "") {
            reduce {
                state.copy(
                    hospitalsFlow = emptyFlow()
                )
            }
        } else {
            val hospitalsFlow = getAllHospitalUseCase(state.search).getOrThrow()
            reduce {
                state.copy(
                    hospitalsFlow = hospitalsFlow ?: emptyFlow()
                )
            }
        }
    }
}


@Immutable
data class PlusLoginState(
    val name: String = "",
    val hospital: UserHospital? = null,
    val search: String = "",
    val empNo: String = "",
    val hospitalsFlow: Flow<PagingData<UserHospital>> = emptyFlow()
)

sealed interface PlusLoginSideEffect {
    class Toast(val message: String): PlusLoginSideEffect
    data object NavigateToMainActivity: PlusLoginSideEffect
    data object NavigateToLoginActivity: PlusLoginSideEffect
}