package org.egglog.api.group.model.repository;

import org.egglog.api.group.model.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long>,GroupCustomQuery {
}
