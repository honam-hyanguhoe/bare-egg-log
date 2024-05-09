package org.egglog.api.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.config.JwtProperties;
import org.egglog.api.security.exception.JwtErrorCode;
import org.egglog.api.security.exception.JwtException;
import org.egglog.api.security.repository.redis.RefreshTokenRepository;
import org.egglog.api.security.repository.redis.UnsafeTokenRepository;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.repository.jpa.UserJpaRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final UnsafeTokenRepository unsafeTokenRepository;
    private final UserJpaRepository userJpaRepository;
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
        log.debug("기간={}",period);
        log.debug("기간={}",Date.from(ZonedDateTime.now(zoneId).plus(Duration.ofMillis(period)).toInstant()));
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
        log.debug("토큰생성={}", id);
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
            log.debug("exception : 잘못된 엑세스 토큰 시그니처");
            throw new JwtException(JwtErrorCode.TOKEN_SIGNATURE_ERROR);
        }catch (ExpiredJwtException e){
            log.debug("exception : 엑세스 토큰 기간 만료");
            throw new JwtException(JwtErrorCode.EXPIRED_ACCESS_TOKEN);
        }catch (UnsupportedJwtException e){
            log.debug("exception : 지원되지 않는 엑세스 토큰");
            throw new JwtException(JwtErrorCode.NOT_SUPPORT_TOKEN);
        }catch (IllegalArgumentException e){
            log.debug("exception : 잘못된 엑세스 토큰");
            throw new JwtException(JwtErrorCode.INVALID_TOKEN);
        }catch (SignatureException e){
            log.debug("exception : 변조된 엑세스 토큰");
            throw new JwtException(JwtErrorCode.TOKEN_SIGNATURE_ERROR);
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
            log.debug("exception : 잘못된 리프레쉬 토큰 시그니처");
            throw new JwtException(JwtErrorCode.INVALID_TOKEN);
        }catch (ExpiredJwtException e){
            log.debug("exception : 리프레쉬 토큰 기간 만료");
            User expiredUser = userJpaRepository.findById(Long.valueOf(e.getClaims().getSubject()))
                    .orElseThrow(() -> new UserException(UserErrorCode.DELETED_USER));
            userJpaRepository.save(expiredUser.doLogout()); //로그아웃 처리
            throw new JwtException(JwtErrorCode.INVALID_TOKEN);
        }catch (UnsupportedJwtException e){
            log.debug("exception : 지원되지 않는 리프레쉬 토큰");
            throw new JwtException(JwtErrorCode.INVALID_TOKEN);
        }catch (IllegalArgumentException e){
            log.debug("exception : 잘못된 리프레쉬 토큰");
            throw new JwtException(JwtErrorCode.INVALID_TOKEN);
        }catch (SignatureException e){
            log.debug("exception : 변조된 엑세스 토큰");
            throw new JwtException(JwtErrorCode.INVALID_TOKEN);
        } catch (Exception e){
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

