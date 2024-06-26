package com.org.egglog.presentation.domain.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.org.egglog.domain.auth.model.UserDetail
import com.org.egglog.domain.auth.model.UserHospital
import com.org.egglog.domain.auth.model.UserModifyParam
import com.org.egglog.domain.auth.usecase.DeleteTokenUseCase
import com.org.egglog.domain.auth.usecase.DeleteUserStoreUseCase
import com.org.egglog.domain.auth.usecase.GetAllHospitalUseCase
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.auth.usecase.SetUserStoreUseCase
import com.org.egglog.domain.auth.usecase.UpdateUserDeleteUseCase
import com.org.egglog.domain.auth.usecase.UpdateUserModifyUseCase
import com.org.egglog.domain.setting.usecase.DeleteCalendarGroupMapStoreUseCase
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
    private val deleteTokenUseCase: DeleteTokenUseCase,
    private val getUserStoreUseCase: GetUserStoreUseCase,
    private val setUserStoreUseCase: SetUserStoreUseCase,
    private val deleteUserStoreUseCase: DeleteUserStoreUseCase,
    private val deleteCalendarGroupMapStoreUseCase: DeleteCalendarGroupMapStoreUseCase,
    private val updateUserDeleteUseCase: UpdateUserDeleteUseCase,
    private val updateUserModifyUseCase: UpdateUserModifyUseCase,
    private val getAllHospitalUseCase: GetAllHospitalUseCase
): ViewModel(), ContainerHost<MySettingState, MySettingSideEffect>{
    override val container: Container<MySettingState, MySettingSideEffect> = container(
        initialState = MySettingState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    if(throwable.message?.contains("HTTP 406") == true) {
                        postSideEffect(MySettingSideEffect.Toast(message = "탈퇴 전 그룹장을 넘겨주세요."))
                    } else {
                        postSideEffect(MySettingSideEffect.Toast(message = throwable.message.orEmpty()))
                    }
                    reduce {
                        state.copy(enabledDelete = true, enabledModify = true)
                    }
                }
            }
        }
    )

    init {
        load()
    }

    private fun load() = intent {
        val user = getUserStoreUseCase()
        reduce {
            state.copy(
                user = user,
                name = user?.userName ?: "",
                hospital = user?.selectedHospital,
                empNo = user?.empNo ?: "",
            )
        }
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

    fun onClickDone() = intent {
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

    fun onClickDelete() = intent {
        reduce {
            state.copy(enabledDelete = false)
        }
        val tokens = getTokenUseCase()
        updateUserDeleteUseCase("Bearer ${tokens.first.orEmpty()}").getOrThrow()
        deleteTokenUseCase()
        deleteUserStoreUseCase()
        deleteCalendarGroupMapStoreUseCase()
        postSideEffect(MySettingSideEffect.Toast("탈퇴가 완료되었습니다."))
        postSideEffect(MySettingSideEffect.NavigateToLoginActivity)
        reduce {
            state.copy(enabledDelete = true)
        }
    }

    fun onClickModify() = intent {
        reduce {
            state.copy(enabledModify = false)
        }
        val tokens = getTokenUseCase()
        val user = updateUserModifyUseCase("Bearer ${tokens.first.orEmpty()}", UserModifyParam(userName = state.name, profileImgUrl = state.user?.profileImgUrl ?: "", selectHospitalId = state.hospital?.hospitalId ?: 0, empNo = state.empNo)).getOrThrow()
        setUserStoreUseCase(user)
        postSideEffect(MySettingSideEffect.Toast("수정이 완료되었습니다."))
        reduce {
            state.copy(enabledModify = true, user = user)
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
    val hospitalsFlow: Flow<PagingData<UserHospital>> = emptyFlow(),
    val enabledModify: Boolean = true,
    val enabledDelete: Boolean = true
)

sealed interface MySettingSideEffect {
    class Toast(val message: String): MySettingSideEffect
    data object NavigateToLoginActivity: MySettingSideEffect
}