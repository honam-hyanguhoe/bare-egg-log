package org.egglog.api.security.repository;

import com.nursetest.app.security.model.entity.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<Token, Long> {
    Optional<Token> findByAccessToken(String accessToken);
}
