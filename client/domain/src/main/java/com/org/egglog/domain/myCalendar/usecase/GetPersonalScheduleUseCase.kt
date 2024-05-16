package com.org.egglog.domain.myCalendar.usecase

import com.org.egglog.domain.myCalendar.model.PersonalScheduleData

interface GetPersonalScheduleUseCase {

    suspend operator fun invoke(accessToken:String, startDate: String, endDate: String, userId: Long, calendarGroupId: Long): Result<List<PersonalScheduleData>>
}