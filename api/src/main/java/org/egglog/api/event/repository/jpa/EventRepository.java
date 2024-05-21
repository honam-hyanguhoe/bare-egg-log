package org.egglog.api.event.repository.jpa;


import org.egglog.api.event.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventCustomQuery {

    @Query("select e from Event e where e.user.id = :userId")
    List<Event> findAllByUserId(Long userId);
}
