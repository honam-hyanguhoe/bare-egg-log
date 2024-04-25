package org.egglog.api.security.util;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.config.JwtProperties;
import org.egglog.api.security.exception.JwtErrorCode;
import org.egglog.api.security.exception.JwtException;
import org.egglog.api.security.repository.redis.UnsafeTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;
    private static final ZoneId zoneId = ZoneId.of("Asia/Seoul");
    private String accessSecretKey;
    private String refreshSecretKey;

    private final UnsafeTokenRepository unsafeTokenRepository;
    @PostConstruct
    protected void init(){
        accessSecretKey = Base64.getEncoder().encodeToString(
                jwtProperties.getAccess().getBytes()
        );
        refreshSecretKey = Base64.getEncoder().encodeToString(
                jwtProperties.getAccess().getBytes()
        );
    }

    //토큰발행시간
    public Date getIssuedAt(){
        return Date.from(ZonedDateTime.now(zoneId).toInstant());
    }
    //토큰만료시간
    public Date getExpiredTime(Long period){
        log.info("기간={}",period);
        log.info("기간={}",Date.from(ZonedDateTime.now(zoneId).plus(Duration.ofMillis(period)).toInstant()));
        return Date.from(ZonedDateTime.now(zoneId).plus(Duration.ofMillis(period)).toInstant());
    }

    //리프레시 토큰 생성
    public String generateRefreshToken(Long id, String role){
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("role", role)
                .setIssuedAt(getIssuedAt())
                .setExpiration(getExpiredTime(jwtProperties.getRefreshtime()))
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
                .compact();
    }

    //엑세스 토큰 생성
    public String generateAccessToken(Long id, String role){
        log.info("토큰생성={}", id);
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("role", role)
                .setIssuedAt(getIssuedAt())
                .setExpiration(getExpiredTime(jwtProperties.getAccesstime()))
                .signWith(SignatureAlgorithm.HS256, accessSecretKey)
                .compact();
    }

    /**
     * 엑세스 토큰 검증
     */
    public Jws<Claims> validateAccessToken(final String token){

        try{
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(token);
            unsafeTokenRepository.findById(token).ifPresent(value -> {
                throw new JwtException(JwtErrorCode.TOKEN_SIGNATURE_ERROR);
            });
            return claimsJws;
        }catch ( MalformedJwtException e){
            log.info("exception : 잘못된 엑세스 토큰 시그니처");
            throw new JwtException(JwtErrorCode.TOKEN_SIGNATURE_ERROR);
        }catch (ExpiredJwtException e){
            log.info("exception : 엑세스 토큰 기간 만료");
            throw new JwtException(JwtErrorCode.EXPIRED_TOKEN);
        }catch (UnsupportedJwtException e){
            log.info("exception : 지원되지 않는 엑세스 토큰");
            throw new JwtException(JwtErrorCode.NOT_SUPPORT_TOKEN);
        }catch (IllegalArgumentException e){
            log.info("exception : 잘못된 엑세스 토큰");
            throw new JwtException(JwtErrorCode.INVALID_TOKEN);
        }
    }

    /**
     * 리프레쉬 토큰 검증
     */
    public boolean validateRefreshToken(final String token) throws JwtException {
        try{
            Jwts.parserBuilder().setSigningKey(refreshSecretKey).build()
                    .parseClaimsJws(token);
            return true;
        }catch (MalformedJwtException e){
            log.info("exception : 잘못된 리프레쉬 토큰 시그니처");
            throw new JwtException(JwtErrorCode.TOKEN_SIGNATURE_ERROR);
        }catch (ExpiredJwtException e){
            log.info("exception : 리프레쉬 토큰 기간 만료");
            throw new JwtException(JwtErrorCode.EXPIRED_TOKEN);
        }catch (UnsupportedJwtException e){
            log.info("exception : 지원되지 않는 리프레쉬 토큰");
            throw new JwtException(JwtErrorCode.NOT_SUPPORT_TOKEN);
        }catch (IllegalArgumentException e){
            log.info("exception : 잘못된 리프레쉬 토큰");
            throw new JwtException(JwtErrorCode.INVALID_TOKEN);
        }
    }

    public Long getUserIdByRefreshToken(String refreshToken){
        return Long.valueOf(
                Jwts.parserBuilder()
                        .setSigningKey(refreshSecretKey)
                        .build()
                        .parseClaimsJws(refreshToken)
                        .getBody()
                        .getSubject()
        );
    }

    public String getUserRoleByRefreshToken(String refreshToken){
        return Jwts.parserBuilder()
                .setSigningKey(refreshSecretKey)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody()
                .get("role", String.class);
    }

    public Long getUserIdByAccessToken(String accessToken){
        return Long.valueOf(
                validateAccessToken(accessToken).getBody().getSubject()
        );
    }

    public String getUserRoleByAccessToken(String accessToken){
        return validateAccessToken(accessToken).getBody().get("role", String.class);
    }

}

