package com.org.egglog.domain.main.usecase

import com.org.egglog.domain.main.model.RemainDuty

interface CountRemainingDutyUseCase {

    suspend operator fun invoke(accessToken: String, date: String, dateType: String) : Result<List<RemainDuty>>
}