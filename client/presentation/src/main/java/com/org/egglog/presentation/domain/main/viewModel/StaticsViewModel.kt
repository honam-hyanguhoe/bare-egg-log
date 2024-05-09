package com.org.egglog.presentation.domain.main.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.org.egglog.domain.auth.usecase.GetTokenUseCase
import com.org.egglog.domain.main.usecase.CountRemainingDutyUseCase
import com.org.egglog.domain.main.usecase.GetWorkStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
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

    init {
        Log.d("stats", "근무 통계 데이터 가져올래")
        getWorkStatsData()
        getRemainData()
    }

    fun getRemainData() = intent {
        val tokens = getTokenUseCase()
        val result = countRemainingDutyUseCase(
            accessToken =  "Bearer ${tokens.first}",
            today = state.today,
            dateType = state.dateType
        )
        Log.d("remain", "viewModel result!!!!!!! $result")
    }

    fun getWorkStatsData() = intent {
        val tokens = getTokenUseCase()

        Log.d("stats", "${state.date} --- ${state.month} ")
        val result = getWorkStatsUseCase(
            accessToken =  "Bearer ${tokens.first}",
            date = state.date,
            month = state.month
        )
        Log.d("stats", "viewModel result!!!!!!! $result")
    }
}

data class StaticState(
    val date: String = LocalDate.now().toString(),
    val month: String = LocalDate.now().toString(),
    val dateType: String = "WEEK",
    val today : String = LocalDate.now().toString()
)

