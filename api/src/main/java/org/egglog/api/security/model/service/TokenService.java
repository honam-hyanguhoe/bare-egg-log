package org.egglog.api.security.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.exception.JwtErrorCode;
import org.egglog.api.security.exception.JwtException;
import org.egglog.api.security.model.dto.response.TokenResponse;
import org.egglog.api.security.model.entity.Token;
import org.egglog.api.security.repository.redis.RefreshTokenRepository;
import org.egglog.api.security.repository.redis.UnsafeTokenRepository;
import org.egglog.api.security.util.JwtUtils;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.repository.jpa.UserJpaRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UnsafeTokenRepository unsafeTokenRepository;
    private final UserJpaRepository userJpaRepository;
    public TokenResponse generatedToken(Long id, String role){
        String accessToken = jwtUtils.generateAccessToken(id, role);
        String refreshToken = jwtUtils.generateRefreshToken(id, role);
        return refreshTokenRepository.save(new Token(id, accessToken, refreshToken)).toResponse();
    }

    public void RemoveToken(Long id){
        Token token = refreshTokenRepository.findById(id).orElseThrow(() -> new JwtException(JwtErrorCode.NOT_EXISTS_TOKEN));
        refreshTokenRepository.delete(token);
    }

    public TokenResponse republishToken(String refreshTokenRequest){
        if(jwtUtils.validateRefreshToken(refreshTokenRequest)){
            //여기 까지 들어온 토큰은 검증이 됨 (몇번이고 발행한 토큰 = 여러명이 같은 ID에 대한 토큰을 가지고 있을 수 있음)
            Long id = jwtUtils.getUserIdByRefreshToken(refreshTokenRequest);
            String role = jwtUtils.getUserRoleByRefreshToken(refreshTokenRequest);
            log.debug("id={}",id);
            log.debug("role={}",role);
            //해당 리프레쉬 토큰으로 발행된 토큰 쌍을 가져온다.(엑세스 토큰이 리프레쉬 될때, 항상 토큰 쌍이 재발행 된다)
            Token token = refreshTokenRepository.findById(id).orElseThrow(() -> new JwtException(JwtErrorCode.NOT_EXISTS_TOKEN));

            //해당 토큰이 블랙리스트에 존재한다면
            if (unsafeTokenRepository.findById(token.getAccessToken()).isPresent()){
                // 같은 ID에 대한 토큰이 두명 이상 가지고 있을 수 있다. -> 토큰 탈취 가능성 있다.
                refreshTokenRepository.delete(token); //토큰 지우고
                User user = userJpaRepository.findById(id).orElseThrow(() -> new UserException(UserErrorCode.DELETED_USER));
                userJpaRepository.save(user.doLogout()); //로그아웃 처리후
                throw new JwtException(JwtErrorCode.INVALID_TOKEN); //로그인 재요청
            }
            if(refreshTokenRequest.equals(token.getRefreshToken())){
                //마지막으로 발행된 리프레쉬 토큰과 요청이 들어온 리프레쉬토큰이 같다면 -> 정상적으로 재발행한다.
                refreshTokenRepository.delete(token);
                return generatedToken(id, role);
            }else {
                //마지막으로 발행된 리프레쉬 토큰과 요청 리프레쉬 토큰이 다르다면 -> 같은 ID에 대한 토큰이 두명 이상 가지고 있는 것이다.
                //마지막으로 발행된 토큰 쌍을 블랙리스트에 넣고, 현재 요청들어온 리프레쉬 토큰은 재 발행을 해주지 않는다.
                RemoveToken(id);//현재 토큰의 유저 ID 토큰은 모두 지운다.
                unsafeTokenRepository.save(token.toUnSafeToken());
                User user = userJpaRepository.findById(id).orElseThrow(() -> new UserException(UserErrorCode.DELETED_USER));
                userJpaRepository.save(user.doLogout()); //로그아웃 처리후
                throw new JwtException(JwtErrorCode.INVALID_TOKEN);//로그인 재 요청
            }
        }
        throw new JwtException(JwtErrorCode.INVALID_TOKEN);
    }

}


