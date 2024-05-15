package org.egglog.api.event.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.event.model.entity.Event;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.egglog.api.event.model.entity.QEvent.event;
import static org.egglog.api.user.model.entity.QUser.user;

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


    /**
     * 한달 개인 일정 조회
     *
     * @param startDate
     * @param endDate
     * @param userId
     * @return
     */
    @Override
    public Optional<List<Event>> findEventsByMonthAndUserId(LocalDate startDate, LocalDate endDate, Long userId, Long calendarGroupId) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(event)
                .join(event.user, user)
                .where(
                        user.id.eq(userId),
                        event.calendarGroup.id.eq(calendarGroupId),
                        event.startDate.between(startDateTime, endDateTime)
                                .or(event.endDate.between(startDateTime, endDateTime))
                )
                .fetch());
    }

    public Optional<List<Event>> findByTargetDate(LocalDateTime targetDate, Long userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(event)
                .join(event.user, user)
                .where(
                        user.id.eq(userId),
                        event.startDate.goe(targetDate).and(event.startDate.loe(targetDate))
                )
                .fetch());
    }

    @Override
    public List<Event> findAllEventByUserId(Long userId){
        return jpaQueryFactory
                .selectFrom(event)
                .where(event.user.id.eq(userId))
                .fetch();
    }

    @Override
    public long deleteAllByCalendarGroupId(Long calendarGroupId) {
        return jpaQueryFactory
                .delete(event)
                .where(event.calendarGroup.id.eq(calendarGroupId))
                .execute();
    }

}
