package org.egglog.api.event.repository.jpa;

import org.egglog.api.event.model.entity.Event;
import org.egglog.api.work.model.entity.Work;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventCustomQuery {

    Optional<List<Event>> findEventsByCalendarGroupId(Long calendarGroupId);

    Optional<List<Event>> findEventsByMonthAndUserId(LocalDateTime startDate, LocalDateTime endDate, Long userId, Long calendarGroupId);

//    Optional<List<Event>> findByMonthAndUserId(LocalDateTime startDate, LocalDateTime endDate, Long userId);

    Optional<List<Event>> findByTargetDate(LocalDateTime targetDate, Long userId);

    List<Event> findAllEventByUserId(Long userId);
    long deleteAllByCalendarGroupId(Long calendarGroupId);
}
