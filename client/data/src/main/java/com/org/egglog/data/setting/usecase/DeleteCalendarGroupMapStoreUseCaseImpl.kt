package com.org.egglog.data.setting.usecase

import com.org.egglog.data.datastore.CalendarGroupMapDataStore
import com.org.egglog.domain.setting.usecase.DeleteCalendarGroupMapStoreUseCase
import javax.inject.Inject

class DeleteCalendarGroupMapStoreUseCaseImpl @Inject constructor(
    private val calendarGroupMapDataStore: CalendarGroupMapDataStore
) : DeleteCalendarGroupMapStoreUseCase {
    override suspend fun invoke(): Unit {
        return calendarGroupMapDataStore.clear()
    }
}