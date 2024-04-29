package org.egglog.api.event.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.event.model.entity.Event;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.egglog.api.event.model.entity.QEvent.event;

@Repository
@RequiredArgsConstructor
public class EventCustomQueryImpl implements EventCustomQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<Event>> findEventsByCalendarGroupId(Long calendarGroupId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(event)
                .where(event.calendarGroup.id.eq(calendarGroupId))
                .fetch());
    }
}
