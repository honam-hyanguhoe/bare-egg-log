package com.org.egglog.presentation.domain.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.model.UserHospital
import com.org.egglog.domain.auth.usecase.DeleteTokenUseCase
import com.org.egglog.domain.auth.usecase.DeleteUserStoreUseCase
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.auth.usecase.PostLogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
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
class MySettingViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserStoreUseCase: GetUserStoreUseCase
): ViewModel(), ContainerHost<MySettingState, MySettingSideEffect>{
    override val container: Container<MySettingState, MySettingSideEffect> = container(
        initialState = MySettingState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(MySettingSideEffect.Toast(message = throwable.message.orEmpty()))
                }
            }
        }
    )

    init {

    }

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
}


@Immutable
data class MySettingState(
    val user: UserDetail? = null,
    val name: String = "",
    val hospital: UserHospital? = null,
    val search: String = "",
    val empNo: String = "",
    val hospitalsFlow: Flow<PagingData<UserHospital>> = emptyFlow()
)

sealed interface MySettingSideEffect {
    class Toast(val message: String): MySettingSideEffect
    data object NavigateToLoginActivity: MySettingSideEffect
}