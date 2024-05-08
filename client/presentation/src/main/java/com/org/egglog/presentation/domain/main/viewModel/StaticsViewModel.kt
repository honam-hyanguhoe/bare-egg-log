package com.org.egglog.presentation.domain.main.viewModel

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
)
    : ViewModel(), ContainerHost<StaticState, Nothing> {
    override val container: Container<StaticState, Nothing> = container(
        initialState = StaticState(),
        buildSettings = {})



    fun getRemainData() = intent {

    }

    fun getWorkStatsData() = intent {
        val tokens = getTokenUseCase()

        getWorkStatsUseCase(
            accessToken = tokens.first ?: "",
            date = state.date,
            month = state.month
        )
    }
}

data class StaticState(
    val date: String = LocalDate.now().toString(),
    val month : String = LocalDate.now().month.toString(),
    val dateType : String = "WEEK"  // or "MONTH"
)

