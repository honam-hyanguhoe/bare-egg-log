package org.egglog.api.alarm.repository.jpa;

import org.egglog.api.alarm.model.entity.Alarm;

import java.util.List;
import java.util.Optional;

public interface AlarmCustomQuery {
    List<Alarm> findAlarmList(Long userId);
}
