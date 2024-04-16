package org.egglog.api.user.repository;

import org.egglog.api.user.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<Users, Long> {
}
