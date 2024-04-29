package org.egglog.api.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.model.service.AuthService;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.security.controller
 * fileName      : AuthController
 * description    : 로그인, 로그아웃, 토큰 재발행을 처리하는 컨트롤러
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-24|김형민|최초 생성|
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/logout")
    public ResponseEntity<MessageUtils> logout(@AuthenticationPrincipal User user){
        authService.logout(user);
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    @PostMapping("/refresh")
    public ResponseEntity<MessageUtils> refresh(@RequestHeader("refresh") String refreshToken){
        return ResponseEntity.ok().body(MessageUtils.success(authService.refresh(refreshToken)));
    }

}
