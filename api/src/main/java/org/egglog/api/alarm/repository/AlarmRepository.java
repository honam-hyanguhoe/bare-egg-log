package org.egglog.api.alarm.repository;

import org.egglog.api.alarm.model.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmCustomQuery {
}
