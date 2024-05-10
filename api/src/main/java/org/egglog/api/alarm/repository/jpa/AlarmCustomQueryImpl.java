package org.egglog.api.alarm.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.alarm.model.entity.Alarm;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.egglog.api.alarm.model.entity.QAlarm.alarm;
import static org.egglog.api.user.model.entity.QUser.user;
import static org.egglog.api.worktype.model.entity.QWorkType.workType;

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

    public Optional<Alarm> findWithUserAndWorkTypeById(Long userId) {
        return Optional.of(jpaQueryFactory
                .selectFrom(alarm)
                .leftJoin(alarm.workType, workType).fetchJoin()
                .leftJoin(alarm.user, user).fetchJoin()
                .where(alarm.user.id.eq(userId))
                .fetchOne());
    }
    public Optional<Alarm> findWithUserById(Long userId) {
        return Optional.of(jpaQueryFactory
                .selectFrom(alarm)
                .leftJoin(alarm.user, user).fetchJoin()
                .where(alarm.user.id.eq(userId))
                .fetchOne());
    }

    public Alarm modifyAlarmStatus(Long alarmId) {
        return jpaQueryFactory
                .selectFrom(alarm)
                .where(alarm.id.eq(alarmId))
                .fetchOne();
    }
}
