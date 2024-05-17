package com.org.egglog.presentation.domain.group.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.group.usecase.GetGroupDutyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MemberManageViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel(),
    ContainerHost<MemberManageState, MemberManageSideEffect> {
    override val container: Container<MemberManageState, MemberManageSideEffect> =
        container(initialState = MemberManageState(), buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(MemberManageSideEffect.Toast(message = throwable.message.orEmpty()))
                }
            }
        })

    val groupId = savedStateHandle.get<Long>("groupId")
        ?: throw IllegalStateException("GroupId must be provided")

    init {
        getGroupMember()  // 그룹 멤버 가져옴
    }

    private fun getGroupMember() = intent {
        val tokens = getTokenUseCase()


    }
}

data class MemberManageState(
    val groupId : Int = 0
)
sealed interface MemberManageSideEffect {
    class Toast(val message: String) : MemberManageSideEffect
}