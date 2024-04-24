package org.egglog.api.security.repository.redis;

import org.egglog.api.security.model.entity.Token;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<Token, Long> {
}

