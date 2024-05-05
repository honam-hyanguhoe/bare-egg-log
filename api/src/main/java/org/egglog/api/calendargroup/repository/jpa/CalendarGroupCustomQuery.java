package org.egglog.api.calendargroup.repository.jpa;

import org.egglog.api.calendargroup.model.entity.CalendarGroup;

import java.util.Optional;

public interface CalendarGroupCustomQuery {
    Optional<CalendarGroup> findCalendarGroupWithUserById(Long calendarGroupId);
}
