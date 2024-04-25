package org.egglog.api.user.repository.jpa;

import org.egglog.api.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserJpaRepository extends JpaRepository<User, Long> {
}
