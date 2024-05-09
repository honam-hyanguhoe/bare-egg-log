package org.egglog.api.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.model.service.AuthService;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.model.entity.enums.AuthProvider;
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

    @PostMapping("/login/{provider}")
    public ResponseEntity<MessageUtils> login(
            @RequestHeader String accessToken,
            @PathVariable(name = "provider") AuthProvider authProvider
            ){
        return ResponseEntity.ok()
                .body(MessageUtils.success(authService.login(accessToken, authProvider)));
    }


    /**
     * 근무 일정 생성
     * @param loginUser 로그인한 유저(JWT 토큰)
     * @return 생성된 근무 일정 객체 응답 리스트
     * @author 김형민
     */
    @PostMapping("/logout")
    public ResponseEntity<MessageUtils> logout(@AuthenticationPrincipal User loginUser){
        authService.logout(loginUser);
        return ResponseEntity.ok().body(MessageUtils.success());
    }

    /**
     * 근무 일정 생성
     * @param refreshToken 헤더에 리프레쉬 토큰
     * @return 새 리프레쉬 토큰
     * @author 김형민
     */
    @PostMapping("/refresh")
    public ResponseEntity<MessageUtils> refresh(@RequestHeader String refreshToken){
        return ResponseEntity.ok().body(MessageUtils.success(authService.refresh(refreshToken)));
    }

}
