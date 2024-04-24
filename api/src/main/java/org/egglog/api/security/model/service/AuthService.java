package org.egglog.api.security.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.model.dto.response.TokenResponse;
import org.egglog.api.user.model.entity.User;
import org.springframework.stereotype.Service;

/**
 * packageName    : org.egglog.api.security.model.service
 * fileName       : AuthService
 * author         : 김형민
 * date           : 2024-04-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-24        김형민       최초 생성
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {
    private final TokenService tokenService;

    public void logout(User user){
        tokenService.RemoveToken(user.getId());
    }

    public TokenResponse refresh(String refreshToken){
        return tokenService.republishToken(refreshToken);
    }

}

