package org.egglog.api.security.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.model.service.AuthService;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

