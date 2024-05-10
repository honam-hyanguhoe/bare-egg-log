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
        _selected.value = newValue
        Log.d("remain", "newValue -- ${newValue} ")

        reduce {
            state.copy(
                dateType = _selected.value
            )
        }
        getRemainData()
    }
    init {
        Log.d("stats", "근무 통계 데이터 가져올래")
        getWorkStatsData()
        getRemainData()
    }


    private fun getRemainData() = intent {
        val tokens = getTokenUseCase()
        val result = countRemainingDutyUseCase(
            accessToken =  "Bearer ${tokens.first}",
            today = state.today,
            dateType = state.dateType
        ).getOrNull()

        if(result != null){
            Log.d("remain", result.toString())

            val gson = Gson()
            val jsonData = gson.toJson(result)

            Log.d("remain", "gson result --- $jsonData")

            reduce {
                state.copy( remainData = jsonData)
            }

        }
    }

    private fun getWorkStatsData() = intent {
        val tokens = getTokenUseCase()

        Log.d("stats", "${state.date} --- ${state.month} ")
        val result = getWorkStatsUseCase(
            accessToken =  "Bearer ${tokens.first}",
            date = state.date,
            month = state.month
        ).getOrNull()

        Log.d("stats", result.toString())

        val gson = Gson()
        val jsonData = gson.toJson(result)

        Log.d("stats", "gson result --- $jsonData")
//        WorkStats(month=2024-05, weeks=[WeekStats(week=1, data=WeekData(DAY=3, EVE=3, NIGHT=0, OFF=3))])
        reduce {
            state.copy( remainData = jsonData)
        }

    }
}

data class StaticState(
    val date: String = LocalDate.now().toString(),
    val month: String = LocalDate.now().toString(),
    val dateType: String = "WEEK",
    val today : String = LocalDate.now().toString(),
    val remainData : String = "",
    val statsData : String = ""
)



