package org.egglog.api.worktype.repository.jpa;

import org.egglog.api.worktype.model.entity.WorkType;

import java.util.List;
import java.util.Optional;

public interface WorkTypeQueryRepository {
    List<WorkType> findListWithUserById(Long workTypeId);
    Optional<WorkType> findWithUserById(Long workTypeId);
}
