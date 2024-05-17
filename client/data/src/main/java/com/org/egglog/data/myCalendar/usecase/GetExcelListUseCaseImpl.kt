package com.org.egglog.data.myCalendar.usecase

import android.util.Log
import com.org.egglog.data.myCalendar.model.toDomainModel
import com.org.egglog.data.myCalendar.service.MyCalendarService
import com.org.egglog.domain.myCalendar.model.ExcelData
import com.org.egglog.domain.myCalendar.usecase.GetExcelListUseCase
import javax.inject.Inject

class GetExcelListUseCaseImpl @Inject constructor(
    private val myCalendarService: MyCalendarService
): GetExcelListUseCase {
    override suspend fun invoke(accessToken: String, date: String): Result<List<ExcelData>> =
        kotlin.runCatching {
            myCalendarService.getExcelList(accessToken, date).dataBody!!.map { it.toDomainModel() }
        }
}