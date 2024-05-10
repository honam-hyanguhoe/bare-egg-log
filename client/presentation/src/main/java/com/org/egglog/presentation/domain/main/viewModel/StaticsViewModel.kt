package com.org.egglog.presentation.domain.main.viewModel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.main.usecase.CountRemainingDutyUseCase
import com.org.egglog.domain.main.usecase.GetWorkStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class StaticsViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getWorkStatsUseCase: GetWorkStatsUseCase,
    private val countRemainingDutyUseCase: CountRemainingDutyUseCase
) : ViewModel(), ContainerHost<StaticState, Nothing> {
    override val container: Container<StaticState, Nothing> = container(
        initialState = StaticState(),
        buildSettings = {})

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
            val jsonData = Gson().toJson(result)
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
            reduce { state.copy(remainData = jsonData) }
        } else {
            Log.e("ViewModel", "Failed to fetch remaining duty data")
        }
    }
    init {
        updateData()
    }
}

data class StaticState(
    val date: String = LocalDate.now().toString(),
    val month: String = LocalDate.now().toString(),
    val dateType: String = "WEEK",
    val today : String = LocalDate.now().toString(),
    val remainData : String = "있니",
    val statsData : String = ""
)


