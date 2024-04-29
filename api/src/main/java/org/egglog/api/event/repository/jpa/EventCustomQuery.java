package org.egglog.api.event.repository.jpa;

import org.egglog.api.event.model.entity.Event;

import java.util.List;
import java.util.Optional;

public interface EventCustomQuery {

    Optional<List<Event>> findEventsByCalendarGroupId(Long calendarGroupId);
}
