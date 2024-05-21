package org.egglog.api.security.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.egglog.api.security.model.dto.response.TokenResponse;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;



@Getter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RedisHash(value = "jwtToken", timeToLive = 86400)
public class Token implements Serializable {
    @Id
    @EqualsAndHashCode.Include
    private Long id;
    @Indexed
    @EqualsAndHashCode.Include
    private String accessToken;
    @EqualsAndHashCode.Include
    private String refreshToken;

    public TokenResponse toResponse(){
        return TokenResponse.builder()
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .build();
    }

    public UnsafeToken toUnSafeToken(){
        return UnsafeToken.builder()
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .build();
    }
}
