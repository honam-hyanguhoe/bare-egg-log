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
    public Optional<List<Alarm>> findAlarmListByUserId(Long userId) {
        return Optional.of(jpaQueryFactory
                .selectFrom(alarm)
                .leftJoin(alarm.workType, workType).fetchJoin()
                .where(alarm.user.id.eq(userId))
                .fetch());
    }
    public Optional<Alarm> findWithUserAndWorkTypeById(Long alarmId) {
        return Optional.of(jpaQueryFactory
                .selectFrom(alarm)
                .leftJoin(alarm.workType, workType).fetchJoin()
                .leftJoin(alarm.user, user).fetchJoin()
                .where(alarm.id.eq(alarmId))
                .fetchOne());
    }
    public Optional<Alarm> findWithUserById(Long alarmId) {
        return Optional.of(jpaQueryFactory
                .selectFrom(alarm)
                .leftJoin(alarm.user, user).fetchJoin()
                .where(alarm.id.eq(alarmId))
                .fetchOne());
    }

    public Alarm modifyAlarmStatus(Long alarmId) {
        return jpaQueryFactory
                .selectFrom(alarm)
                .where(alarm.id.eq(alarmId))
                .fetchOne();
    }

    @Override
    public Optional<Alarm> findByWorkTypeId(Long workTypeId) {
        return Optional.of(jpaQueryFactory
                .selectFrom(alarm)
                .where(alarm.workType.id.eq(workTypeId))
                .fetchOne());
    }
}
