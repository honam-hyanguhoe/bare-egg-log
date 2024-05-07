package org.egglog.api.calendargroup.repository.jpa;

import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.event.model.entity.Event;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface CalendarGroupCustomQuery {
    Optional<CalendarGroup> findCalendarGroupWithUserById(Long calendarGroupId);
    List<CalendarGroup> findListByUserId(Long userId);
    Map<CalendarGroup, Set<String>> findCalendarGroupsWithEventUuids(Long userId);
}
