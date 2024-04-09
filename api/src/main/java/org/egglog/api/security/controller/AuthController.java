package org.egglog.api.security.controller;

import com.nursetest.app.global.util.MessageUtils;
import com.nursetest.app.security.model.dto.request.LoginRequest;
import com.nursetest.app.security.model.dto.request.RefreshTokenRequest;
import com.nursetest.app.security.model.service.AuthService;
import com.nursetest.app.security.model.service.TokenService;
import com.nursetest.app.user.model.dto.response.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/auth")
public class AuthController {
    private final TokenService tokenService;
    private final AuthService authService;


    /**
     *
     * @param loginRequest
     * @return GeneratedToken
     */
    @PostMapping("/login")
    public ResponseEntity<MessageUtils> generateToken(
            @RequestBody LoginRequest loginRequest
            ){
        return ResponseEntity.ok(MessageUtils.success(authService.login(loginRequest.getEmail(),loginRequest.getPassword())));
    }

    /**
     * 토큰 재발행
     * @return GeneratedToken
     */
    @PostMapping("/refresh")
    public ResponseEntity<MessageUtils> refreshToken(
            @RequestBody RefreshTokenRequest refreshTokenRequest
            ){
        return ResponseEntity.ok(MessageUtils.success(tokenService.republishToken(refreshTokenRequest.getRefreshToken())));
    }

    /**
     * 토큰 만료
     * @param
     * @return
     */
    @PostMapping("/logout")
    public ResponseEntity<MessageUtils> logout(
            @AuthenticationPrincipal UserDto userDto
            ){
        log.debug("userDto={}",userDto);
        tokenService.removeToken(userDto.getId());
        return ResponseEntity.ok(MessageUtils.success());
    }

}
