package org.egglog.api.user.repository.jpa;

import org.egglog.api.user.model.entity.User;

import java.util.Optional;

public interface UserQueryRepository {
    Optional<User> findByIdWithHospital(Long userId);
    Optional<User> findByEmailWithHospital(String email);
}
