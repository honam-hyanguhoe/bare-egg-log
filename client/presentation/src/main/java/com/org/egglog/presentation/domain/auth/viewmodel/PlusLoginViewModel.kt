package com.org.egglog.presentation.domain.auth.viewmodel

import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.model.UserHospital
import com.org.egglog.domain.auth.usecase.DeleteTokenUseCase
import com.org.egglog.domain.auth.usecase.DeleteUserStoreUseCase
import com.org.egglog.domain.auth.usecase.SetUserStoreUseCase
import com.org.egglog.domain.auth.usecase.UpdateUserJoinUseCase
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
class PlusLoginViewModel @Inject constructor(
    private val deleteUserStoreUseCase: DeleteUserStoreUseCase,
    private val deleteTokenUseCase: DeleteTokenUseCase,
    private val setUserStoreUseCase: SetUserStoreUseCase,
    private val updateUserJoinUseCase: UpdateUserJoinUseCase
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
    fun onHospitalChange(hospital: UserHospital) = blockingIntent {
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
        // TODO
        // 1. 유저 회원가입
        // 1-1) 성공 시
        // 유저 store에 저장 후 메인으로
        postSideEffect(PlusLoginSideEffect.NavigateToMainActivity)
        // 1-2) 실패 시
        // Toast 띄우고 그대로 두기
    }
}


@Immutable
data class PlusLoginState(
    val name: String = "",
    val hospital: UserHospital? = null,
    val empNo: String = ""
)

sealed interface PlusLoginSideEffect {
    class Toast(val message: String): PlusLoginSideEffect
    data object NavigateToMainActivity: PlusLoginSideEffect
    data object NavigateToLoginActivity: PlusLoginSideEffect
}