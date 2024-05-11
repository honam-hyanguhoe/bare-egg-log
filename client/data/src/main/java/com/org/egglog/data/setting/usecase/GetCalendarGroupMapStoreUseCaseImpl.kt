package com.org.egglog.data.setting.usecase

import com.org.egglog.data.datastore.CalendarGroupMapDataStore
import com.org.egglog.domain.setting.usecase.GetCalendarGroupMapStoreUseCase
import javax.inject.Inject

class GetCalendarGroupMapStoreUseCaseImpl @Inject constructor(
    private val calendarGroupMapDataStore: CalendarGroupMapDataStore
): GetCalendarGroupMapStoreUseCase {
    override suspend fun invoke(): Map<String, Boolean> {
        return calendarGroupMapDataStore.getMap()
    }
}