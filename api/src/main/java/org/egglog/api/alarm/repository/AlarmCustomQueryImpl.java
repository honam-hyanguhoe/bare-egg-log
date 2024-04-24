package org.egglog.api.alarm.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.alarm.model.entity.Alarm;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.egglog.api.alarm.model.entity.QAlarm.alarm;

@Repository
@RequiredArgsConstructor
public class AlarmCustomQueryImpl implements AlarmCustomQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Alarm> findAlarmList(Long userId) {
        return jpaQueryFactory
                .selectFrom(alarm)
                .where(alarm.user.id.eq(userId))
                .fetch();
    }

    public Alarm modifyAlarmStatus(Long alarmId) {
        return jpaQueryFactory
                .selectFrom(alarm)
                .where(alarm.id.eq(alarmId))
                .fetchOne();
    }
}
