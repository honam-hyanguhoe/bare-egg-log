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
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtils jwtUtils;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UnsafeTokenRepository unsafeTokenRepository;

    public TokenResponse generatedToken(Long id, String role){
        String accessToken = jwtUtils.generateAccessToken(id, role);
        String refreshToken = jwtUtils.generateRefreshToken(id, role);
        return refreshTokenRepository.save(new Token(id, accessToken, refreshToken)).toResponse();
    }

    public void RemoveToken(Long id){
        Token token = refreshTokenRepository.findById(id).orElseThrow(() -> new JwtException(JwtErrorCode.NOT_EXISTS_TOKEN));
        refreshTokenRepository.delete(token);
    }

    public TokenResponse republishToken(String refreshToken){
        if(jwtUtils.validateRefreshToken(refreshToken)){
            Long id = jwtUtils.getUserIdByRefreshToken(refreshToken);
            String role = jwtUtils.getUserRoleByRefreshToken(refreshToken);
            Token token = refreshTokenRepository.findById(id).orElseThrow(() -> new JwtException(JwtErrorCode.NOT_EXISTS_TOKEN));

            //블랙리스트에 존재한다면
            if (!unsafeTokenRepository.findById(token.getAccessToken()).isPresent()){
                refreshTokenRepository.delete(token); //토큰 지우고
                throw new JwtException(JwtErrorCode.INVALID_TOKEN); //로그인 재요청
            }
            if(refreshToken.equals(token.getRefreshToken())){
                refreshTokenRepository.delete(token);
                return generatedToken(id, role);
            }else {
                RemoveToken(id);
                unsafeTokenRepository.save(token.toUnSafeToken());
                throw new JwtException(JwtErrorCode.INVALID_TOKEN);
            }
        }
        throw new JwtException(JwtErrorCode.INVALID_TOKEN);
    }

}


