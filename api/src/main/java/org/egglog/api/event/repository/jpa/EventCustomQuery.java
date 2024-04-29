package org.egglog.api.event.repository.jpa;

import org.egglog.api.event.model.entity.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventCustomQuery {

    Optional<List<Event>> findEventsByCalendarGroupId(Long calendarGroupId);

    Optional<List<Event>> findByMonthAndUserId(LocalDateTime startDate, LocalDateTime endDate, Long userId);

    Optional<List<Event>> findByTargetDate(LocalDateTime targetDate, Long userId);

}
