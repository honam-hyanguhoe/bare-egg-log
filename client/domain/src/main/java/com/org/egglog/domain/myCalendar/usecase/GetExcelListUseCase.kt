package com.org.egglog.domain.myCalendar.usecase

import com.org.egglog.domain.myCalendar.model.ExcelData

interface GetExcelListUseCase {

    suspend operator fun invoke(accessToken: String, date: String): Result<List<ExcelData>>
}