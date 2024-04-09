package org.egglog.api.security.util;

import com.nursetest.app.security.config.JwtProperties;
import com.nursetest.app.security.exception.JwtErrorCode;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private static final Long refreshPeriod = 1000L * 60L * 60L * 24L * 7;
    private static final Long accessPeriod = 1000L * 60L * 30L;
    private static final ZoneId  zoneId = ZoneId.of("Asia/Seoul");
    private String accessSecretKey;
    private String refreshSecretKey;

    @PostConstruct
    protected void init(){
        accessSecretKey = Base64.getEncoder().encodeToString(
                jwtProperties.getAccess().getBytes()
        );
        refreshSecretKey = Base64.getEncoder().encodeToString(
                jwtProperties.getRefresh().getBytes()
        );
    }

    /**
     * 토큰 발행 시간
     * @return
     */
    public Date getIssuedAt(){
        return Date.from(ZonedDateTime.now(zoneId)
                .toInstant());
    }



    /**
     * 토큰 만료시간
     * @param period
     * @return
     */
    public Date getExpiredTime(Long period){
        return Date.from(ZonedDateTime.now(zoneId)
                .plus(Duration.ofMillis(period))
                .toInstant());
    }



    /**
     * 리프레쉬 토큰 생성
     */
    public String generateRefreshToken(Long id, String role){
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("role", role)
                .setIssuedAt(getIssuedAt())
                .setExpiration(getExpiredTime(refreshPeriod))
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)
                .compact();
    }

    /**
     * 엑세스 토큰 생성
     */
    public String generateAccessToken(Long id, String role){
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .claim("role", role)
                .setIssuedAt(getIssuedAt())
                .setExpiration(getExpiredTime(accessPeriod))
                .signWith(SignatureAlgorithm.HS256, accessSecretKey)
                .compact();
    }

    /**
     * 엑세스 토큰 검증
     */
    public Jws<Claims> validateAccessToken(final String token){
        try{
            return Jwts.parser().setSigningKey(accessSecretKey).parseClaimsJws(token);
        }catch (SignatureException | MalformedJwtException e){
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
    public boolean validateRefreshToken(final String token){
        try{
            Jwts.parserBuilder().setSigningKey(refreshSecretKey).build()
                    .parseClaimsJws(token);
            return true;
        }catch (SignatureException | MalformedJwtException e){
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
                        .getSubject());
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
        return Long.valueOf(validateAccessToken(accessToken).getBody().getSubject());
    }
    public String getUserRoleByAccessToken(String accessToken){
        return validateAccessToken(accessToken).getBody()
                .get("role", String.class);
    }
}
