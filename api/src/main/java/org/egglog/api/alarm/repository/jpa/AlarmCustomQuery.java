package org.egglog.api.alarm.repository.jpa;

import org.egglog.api.alarm.model.entity.Alarm;

import java.util.List;
import java.util.Optional;

public interface AlarmCustomQuery {
    Optional<List<Alarm>> findAlarmListByUserId(Long userId);
    Optional<Alarm> findWithUserAndWorkTypeById(Long userId);
    Optional<Alarm> findWithUserById(Long userId);
}
