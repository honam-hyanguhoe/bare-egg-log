package org.egglog.api.calendargroup.repository.jpa;

import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.event.repository.jpa.EventCustomQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarGroupRepository extends JpaRepository<CalendarGroup, Long>, EventCustomQuery {
}
