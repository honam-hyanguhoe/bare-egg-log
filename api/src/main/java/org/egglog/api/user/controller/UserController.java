package org.egglog.api.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.egglog.api.user.model.dto.request.JoinUserRequest;
import org.egglog.api.user.model.dto.request.UpdateUserHospitalRequest;
import org.egglog.api.user.model.dto.request.UpdateUserRequest;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.model.service.UserService;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/find")
    public ResponseEntity<MessageUtils> findUser(
            @AuthenticationPrincipal User loginUser
    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(loginUser.toResponse()));
    }

    @PatchMapping("/join")
    public ResponseEntity<MessageUtils> joinUser(
            @AuthenticationPrincipal User loginUser,
            @RequestBody JoinUserRequest request
            ){

        return ResponseEntity.ok().body(
                MessageUtils.success(userService.joinUser(loginUser, request)));
    }

    @PatchMapping("/info-modify")
    public ResponseEntity<MessageUtils> modifyUser(
            @AuthenticationPrincipal User loginUser,
            @RequestBody UpdateUserRequest request
            ){
        return ResponseEntity.ok().body(
                MessageUtils.success(userService.updateUser(loginUser, request)));
    }

    @PatchMapping("/hospital/info-modify")
    public ResponseEntity<MessageUtils> modifyUser(
            @AuthenticationPrincipal User loginUser,
            @RequestBody UpdateUserHospitalRequest request
    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(userService.updateUserHospital(loginUser, request)));
    }

    @PatchMapping("/delete")
    public ResponseEntity<MessageUtils> modifyUserStateDelete(
            @AuthenticationPrincipal User loginUser
    ){
        return ResponseEntity.ok(MessageUtils.success(userService.deleteUser(loginUser)));
    }

}
