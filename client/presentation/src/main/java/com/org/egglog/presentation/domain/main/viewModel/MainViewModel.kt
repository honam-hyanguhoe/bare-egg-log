package com.org.egglog.presentation.domain.main.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.auth.usecase.GetUserStoreUseCase
import com.org.egglog.domain.main.usecase.GetWeeklyWorkUseCase
import com.org.egglog.presentation.domain.community.posteditor.viewmodel.PostSideEffect
import com.org.egglog.presentation.domain.community.posteditor.viewmodel.PostState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getWeeklyWorkUseCase: GetWeeklyWorkUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val getUserStoreUseCase: GetUserStoreUseCase
) : ViewModel(), ContainerHost<MainState, MainSideEffect> {
    override val container: Container<MainState, MainSideEffect> =
        container(initialState = MainState(), buildSettings = {})

    private val sideEffectMappings = listOf(
        MainSideEffect.NavigateToCalendarScreen,
        MainSideEffect.NavigateToGroupScreen,
        MainSideEffect.NavigateToHomeScreen,
        MainSideEffect.NavigateToCommunityScreen,
        MainSideEffect.NavigateToSettingScreen
    )

    // bottom navigation 로직 구현

}

data class MainState(
    val selectedItem : Int = 2
)

sealed class MainSideEffect {
    class Toast(val message: String) : MainSideEffect()
    object NavigateToCalendarScreen : MainSideEffect()
    object NavigateToGroupScreen : MainSideEffect()
    object NavigateToHomeScreen : MainSideEffect()
    object NavigateToCommunityScreen : MainSideEffect()
    object NavigateToSettingScreen : MainSideEffect()
}

//0 -> CalendarPage()
//1 -> GroupPage()
//2 -> HomePage()
//3 -> CommunityPage()
//4 -> SettingsPage()