package org.egglog.api.work.repository.jpa;

import org.egglog.api.user.model.entity.User;
import org.egglog.api.work.model.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkJpaRepository extends JpaRepository<Work, Long> {
}
