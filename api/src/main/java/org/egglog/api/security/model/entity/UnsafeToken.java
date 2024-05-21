package org.egglog.api.security.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RedisHash(value = "blackList", timeToLive = 86400)
public class UnsafeToken {
    @Id
    @EqualsAndHashCode.Include
    private String accessToken;
    @EqualsAndHashCode.Include
    private String refreshToken;
}
