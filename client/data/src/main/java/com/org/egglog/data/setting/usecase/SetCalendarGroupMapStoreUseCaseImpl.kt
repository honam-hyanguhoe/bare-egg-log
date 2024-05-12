package com.org.egglog.data.setting.usecase

import com.org.egglog.data.datastore.CalendarGroupMapDataStore
import com.org.egglog.domain.setting.usecase.SetCalendarGroupMapStoreUseCase
import javax.inject.Inject

class SetCalendarGroupMapStoreUseCaseImpl @Inject constructor(
    private val calendarGroupMapDataStore: CalendarGroupMapDataStore
): SetCalendarGroupMapStoreUseCase {
    override suspend fun invoke(map: Map<String, Boolean> ) {
        calendarGroupMapDataStore.setMap(map)
    }
}