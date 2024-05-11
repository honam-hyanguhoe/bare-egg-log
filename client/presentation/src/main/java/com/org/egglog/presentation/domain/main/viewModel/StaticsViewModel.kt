package com.org.egglog.presentation.domain.main.viewModel

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.main.usecase.CountRemainingDutyUseCase
import com.org.egglog.domain.main.usecase.GetWorkStatsUseCase
import com.org.egglog.presentation.domain.setting.viewmodel.SettingSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StaticsViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getWorkStatsUseCase: GetWorkStatsUseCase,
    private val countRemainingDutyUseCase: CountRemainingDutyUseCase
) : ViewModel(), ContainerHost<StaticState, StaticSideEffect> {
    override val container: Container<StaticState, StaticSideEffect> = container(
        initialState = StaticState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                intent {
                    postSideEffect(StaticSideEffect.Toast(message = throwable.message.orEmpty()))
                }
            }
        })

    private val _selected = mutableStateOf("Week")
    val selected: MutableState<String> = _selected

    fun setSelected(newValue: String) = intent {
        reduce { state.copy(dateType = newValue) }
        updateData()
    }

    private fun updateData() = intent {
        val tokenResult = getTokenUseCase().first
        if (tokenResult != null) {
            getWorkStatsData("Bearer ${tokenResult}")
            getRemainData("Bearer ${tokenResult}")
        } else {
            Log.e("webview", "Failed to fetch token")
        }
    }

    private fun getWorkStatsData(accessToken: String) = intent {
        val result = getWorkStatsUseCase(
            accessToken = accessToken,
            date = state.date,
            month = state.month
        ).getOrNull()
        if (result != null) {
            Log.d("web-stats", "result ${result}")
            val jsonData = Gson().toJson(result)
            Log.d("web-stats", "view model ${jsonData}")
            reduce { state.copy(statsData = jsonData) }
        } else {
            Log.e("webview", "Failed to fetch work stats data")
        }
    }

    private fun getRemainData(accessToken: String) = intent {
        val result = countRemainingDutyUseCase(
            accessToken = accessToken,
            today = state.today,
            dateType = state.dateType
        ).getOrNull()
        if (result != null) {
            val jsonData = Gson().toJson(result)
            Log.d("remain", "view model--- ${jsonData}")
            reduce { state.copy(remainData = jsonData) }
        } else {
            Log.e("ViewModel", "Failed to fetch remaining duty data")
        }
    }
    init {
        updateData()
    }

    fun onSelectedIdx(selectedIdx: Int) = intent {
        when(selectedIdx) {
            0 -> postSideEffect(StaticSideEffect.Toast("출시 준비 중입니다."))
            1 -> postSideEffect(StaticSideEffect.NavigateToGroupActivity)
            3 -> postSideEffect(StaticSideEffect.Toast("출시 준비 중입니다."))
            4 -> postSideEffect(StaticSideEffect.NavigateToSettingActivity)
        }
    }
}

@Immutable
data class StaticState(
    val date: String = LocalDate.now().toString(),
    val month: String = LocalDate.now().toString(),
    val dateType: String = "WEEK",
    val today : String = LocalDate.now().toString(),
    val remainData : String = "있니",
    val statsData : String = "",
    val selectedIdx: Int = 2
)

sealed interface StaticSideEffect {
    class Toast(val message: String): StaticSideEffect
    data object NavigateToSettingActivity: StaticSideEffect
    data object NavigateToCommunityActivity: StaticSideEffect
    data object NavigateToGroupActivity: StaticSideEffect

}
