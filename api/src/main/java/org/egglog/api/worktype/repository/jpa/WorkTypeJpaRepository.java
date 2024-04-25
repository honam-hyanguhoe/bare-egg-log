package org.egglog.api.worktype.repository.jpa;

import org.egglog.api.worktype.model.entity.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkTypeJpaRepository extends JpaRepository<WorkType, Long> {

    @Query("select wt from WorkType wt where wt.user.id = :userId")
    List<WorkType> findWorkTypesByUserId(Long userId);
}
