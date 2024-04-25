package org.egglog.api.calendar.model.repository;


import org.egglog.api.calendar.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("select sh from Schedule sh where sh.user.id = :userId")
    List<Schedule> findAllByUserId(Long userId);
}
