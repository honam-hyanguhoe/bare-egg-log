package com.org.egglog.data.main.usecase

import com.org.egglog.domain.main.usecase.CountRemainingDutyUseCase
import javax.inject.Inject

class CountRemainingDutyUseCaseImpl @Inject constructor() : CountRemainingDutyUseCase{
    override suspend fun invoke(date: String, dateType: String) {
        TODO("Not yet implemented")
    }
}