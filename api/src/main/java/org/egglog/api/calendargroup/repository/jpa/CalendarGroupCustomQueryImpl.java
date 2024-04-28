package org.egglog.api.calendargroup.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.group.model.entity.GroupMember;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.egglog.api.calendargroup.model.entity.QCalendarGroup.calendarGroup;

@Repository
@RequiredArgsConstructor
public class CalendarGroupCustomQueryImpl implements CalendarGroupCustomQuery {

    private final JPAQueryFactory jpaQueryFactory;
    private final CalendarGroupRepository calendarGroupRepository;


}
