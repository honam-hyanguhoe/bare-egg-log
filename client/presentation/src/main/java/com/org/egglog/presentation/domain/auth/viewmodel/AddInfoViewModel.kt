package com.org.egglog.presentation.domain.auth.viewmodel

import androidx.lifecycle.ViewModel
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
class AddInfoViewModel @Inject constructor(

): ViewModel(), ContainerHost<AddInfoState, AddInfoSideEffect>{
    override val container: Container<AddInfoState, AddInfoSideEffect> = container(
        initialState = AddInfoState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(AddInfoSideEffect.Toast(message = throwable.message.orEmpty()))
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
    fun onHospitalChange(hospital: String) = blockingIntent {
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

    /* TODO{
        2. x 버튼 클릭 시 LoginActivity로 이동시키기
        3. AgreeScreen에서 < 버튼 클릭 시 LoginActivity로 이동시키기
        4. AddInfoScreen에서 < 버튼 클릭시 AgreeScreen으로 이동시키기(얘는 왜 안 됨???)
        5. 병원 근무지 입력 어떻게 될지 함 확인하기
        6. AddInfoScreen에서 회원가입 완료 버튼 클릭할 때 작동 로직 짜기
    } */
}


@Immutable
data class AddInfoState(
    val name: String = "",
    val hospital: String = "",
    val empNo: String = ""

)

sealed interface AddInfoSideEffect {
    class Toast(val message: String): AddInfoSideEffect
    data object NavigateToMainActivity: AddInfoSideEffect
}