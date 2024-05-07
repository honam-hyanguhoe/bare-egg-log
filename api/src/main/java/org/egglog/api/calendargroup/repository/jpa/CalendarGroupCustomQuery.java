package org.egglog.api.calendargroup.repository.jpa;

import org.egglog.api.calendargroup.model.entity.CalendarGroup;

import java.util.List;
import java.util.Optional;

public interface CalendarGroupCustomQuery {
    Optional<CalendarGroup> findCalendarGroupWithUserById(Long calendarGroupId);

    List<CalendarGroup> findListByUserId(Long userId);
    List<CalendarGroup> findUrlNotNullListByUserId(Long userId);
}
