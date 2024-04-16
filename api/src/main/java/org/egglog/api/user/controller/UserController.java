//package org.egglog.api.user.controller;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.egglog.api.global.util.MessageUtils;
//import org.egglog.api.user.model.dto.request.*;
//import org.egglog.api.user.model.dto.response.UserDto;
//import org.egglog.api.user.model.dto.response.UserResponse;
//import org.egglog.api.user.model.entity.enums.UserStatus;
//import org.egglog.api.user.model.service.UserService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/v1/user")
//@RequiredArgsConstructor
//@Slf4j
//public class UserController {
//    private final UserService userService;
//
//    @PostMapping("/join")
//    public ResponseEntity<MessageUtils> joinUser(
//            @RequestBody JoinUserRequest joinUserRequest
//            ){
//        userService.join(joinUserRequest);
//        return ResponseEntity.ok().body(MessageUtils.success());
//    }
//
//    @GetMapping("/find")
//    public ResponseEntity<MessageUtils> findUser(
//            @ModelAttribute FindUserRequest findUserRequest,
//            @AuthenticationPrincipal UserDto userDto
//    ){
//        log.debug("findUserRequest={}",findUserRequest);
//        return ResponseEntity.ok().body(
//                MessageUtils.success(UserResponse.of(userDto, findUserRequest)));
//    }
//
//    @PatchMapping("/modify")
//    public ResponseEntity<MessageUtils> modifyUser(
//            @AuthenticationPrincipal UserDto userDto,
//            @RequestBody EditUserRequest editUserData
//            ){
//        userService.modify(userDto.getId(), editUserData);
//        return ResponseEntity.ok().body(
//                MessageUtils.success());
//    }
//    @GetMapping("/find-email")
//    public ResponseEntity<MessageUtils> findEmail(
//            @ModelAttribute FindEmailRequest emailRequest
//            ){
//        return ResponseEntity.ok(
//                MessageUtils.success(
//                        UserResponse.of(userService.findEmail(emailRequest).getEmail())));
//    }
//
//
//    @PatchMapping("/new-password")
//    public ResponseEntity<MessageUtils> editPwd(
//            @AuthenticationPrincipal UserDto userDto,
//            @RequestBody RefreshPasswordRequest refreshPasswordRequest
//            ){
//        userService.editPassword(userDto.getId(), refreshPasswordRequest);
//        return ResponseEntity.ok(MessageUtils.success());
//    }
//
//    @PatchMapping("/delete")
//    public ResponseEntity<MessageUtils> modifyUserStateDelete(
//            @AuthenticationPrincipal UserDto userDto
//    ){
//        userService.modifyUserState(userDto.getId(), UserStatus.DELETED);
//        return ResponseEntity.ok(MessageUtils.success());
//    }
//
//}
