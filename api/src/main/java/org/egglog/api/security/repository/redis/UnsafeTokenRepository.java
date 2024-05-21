package org.egglog.api.security.repository.redis;

import org.egglog.api.security.model.entity.UnsafeToken;
import org.springframework.data.repository.CrudRepository;

public interface UnsafeTokenRepository extends CrudRepository<UnsafeToken, String> {
}

