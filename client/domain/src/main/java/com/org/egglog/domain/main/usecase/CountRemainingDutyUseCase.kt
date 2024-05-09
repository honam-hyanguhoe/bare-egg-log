package com.org.egglog.domain.main.usecase

import com.org.egglog.domain.main.model.RemainDuty

interface CountRemainingDutyUseCase {

    suspend operator fun invoke(accessToken: String, today: String, dateType: String) : Result<List<RemainDuty>>
}