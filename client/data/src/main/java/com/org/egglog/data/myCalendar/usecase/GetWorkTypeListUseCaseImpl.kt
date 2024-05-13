package com.org.egglog.data.myCalendar.usecase

import com.org.egglog.data.myCalendar.model.toDomainModel
import com.org.egglog.data.myCalendar.service.MyCalendarService
import com.org.egglog.domain.myCalendar.model.WorkType
import com.org.egglog.domain.myCalendar.usecase.GetWorkTypeListUseCase
import javax.inject.Inject

class GetWorkTypeListUseCaseImpl @Inject constructor(
    private val myCalendarService: MyCalendarService
): GetWorkTypeListUseCase {
    override suspend fun invoke(accessToken: String): Result<List<WorkType>> = kotlin.runCatching {
        myCalendarService.getWorkTypeList(accessToken).dataBody!!.map {it.toDomainModel()}
    }

}