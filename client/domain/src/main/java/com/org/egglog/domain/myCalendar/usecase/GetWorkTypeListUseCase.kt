package com.org.egglog.domain.myCalendar.usecase

import com.org.egglog.domain.myCalendar.model.WorkType

interface GetWorkTypeListUseCase {

    suspend operator fun invoke(accessToken: String): Result<List<WorkType>>
}