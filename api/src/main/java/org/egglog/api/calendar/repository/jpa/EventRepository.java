package org.egglog.api.calendar.repository.jpa;


import org.egglog.api.calendar.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventCustomQuery{

    @Query("select e from Event e where e.user.id = :userId")
    List<Event> findAllByUserId(Long userId);
}
