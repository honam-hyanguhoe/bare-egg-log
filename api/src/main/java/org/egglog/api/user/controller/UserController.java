package org.egglog.api.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.egglog.api.user.model.dto.request.JoinUserRequest;
import org.egglog.api.user.model.dto.request.UpdateFcmRequest;
import org.egglog.api.user.model.dto.request.UpdateUserHospitalRequest;
import org.egglog.api.user.model.dto.request.UpdateUserRequest;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.model.service.UserService;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.user.cotroller
 * fileName       : UserController
 * description    : 회원 컨트롤러
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-20|김형민|최초 생성|
 * |2024-05-02|김형민|주석 추가|
 */
@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    /**
     * 유저 정보 조회
     * @param loginUser JWT 토큰
     * @return 유저 ID, 이메일, 이름, 선택된 병원, 선택된 병원에 대한 인증 정보(인증 요청이 없다면 null), 프로필이미지, 유저권한, 유저 상태, 활성 디바이스토큰, 사번, 생성일, 마지막 수정일, 마지막 로그인일
     * @author 김형민
     */
    @GetMapping("/find")
    public ResponseEntity<MessageUtils> findUser(
            @AuthenticationPrincipal User loginUser
    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(userService.find(loginUser)));
    }

    /**
     * 유저 회원 가입
     * @param loginUser JWT 토큰
     * @param request 유저이름, 선택된 병원 Id, 사번, 가입시 FCM 토큰
     * @return 가입된 유저 ID, 이메일, 이름, 선택된 병원, 선택된 병원에 대한 인증 정보(인증 요청이 없다면 null), 프로필이미지, 유저권한, 유저 상태, 활성 디바이스토큰, 사번, 생성일, 마지막 수정일, 마지막 로그인일
     * @author 김형민
     */
    @PatchMapping("/join")
    public ResponseEntity<MessageUtils> joinUser(
            @AuthenticationPrincipal User loginUser,
            @RequestBody JoinUserRequest request
            ){
        return ResponseEntity.ok().body(
                MessageUtils.success(userService.joinUser(loginUser, request)));
    }

    /**
     * 유저 활성(알림) 디바이스 변경
     * @param loginUser JWT 토큰
     * @param request fcm 토큰
     * @return 변경된 유저 ID, 이메일, 이름, 선택된 병원, 선택된 병원에 대한 인증 정보(없다면 null), 프로필이미지, 유저권한, 유저 상태, 활성 디바이스토큰, 사번, 생성일, 마지막 수정일, 마지막 로그인일
     * @author 김형민
     */
    @PatchMapping("/fcm-modify")
    public ResponseEntity<MessageUtils> modifyUserFcm(
            @AuthenticationPrincipal User loginUser,
            @RequestBody UpdateFcmRequest request
    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(userService.updateFcmUser(loginUser, request)));
    }

    /**
     * 유저 정보 변경
     * @param loginUser JWT 토큰
     * @param request 유저 이름, 유저 프로필 사진
     * @return 변경된 유저 ID, 이메일, 이름, 선택된 병원, 선택된 병원에 대한 인증 정보(없다면 null), 프로필이미지, 유저권한, 유저 상태, 활성 디바이스토큰, 사번, 생성일, 마지막 수정일, 마지막 로그인일
     * @author 김형민
     */
    @PatchMapping("/info-modify")
    public ResponseEntity<MessageUtils> modifyUserInfo(
            @AuthenticationPrincipal User loginUser,
            @RequestBody UpdateUserRequest request
            ){
        return ResponseEntity.ok().body(
                MessageUtils.success(userService.updateUserInfo(loginUser, request)));
    }

    /**
     * 유저 병원 정보 변경
     * @param loginUser JWT 토큰
     * @param request 변경할 병원 ID, 사번
     * @return 변경된 유저 ID, 이메일, 이름, 선택된 병원, 선택된 병원에 대한 인증 정보(없다면 null), 프로필이미지, 유저권한, 유저 상태, 활성 디바이스토큰, 사번, 생성일, 마지막 수정일, 마지막 로그인일
     * @author 김형민
     */
    @PatchMapping("/hospital/info-modify")
    public ResponseEntity<MessageUtils> modifyHospitalUserInfo(
            @AuthenticationPrincipal User loginUser,
            @RequestBody UpdateUserHospitalRequest request
    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(userService.updateUserHospital(loginUser, request)));
    }

    /**
     * 유저 탈퇴
     * @param loginUser JWT 토큰
     * @return 삭제된 상태의 유저 ID, 이메일, 이름, 선택된 병원, 선택된 병원에 대한 인증 정보(없다면 null), 프로필이미지, 유저권한, 유저 상태, 활성 디바이스토큰, 사번, 생성일, 마지막 수정일, 마지막 로그인일
     * @author 김형민
     */
    @PatchMapping("/delete")
    public ResponseEntity<MessageUtils> modifyUserStateDelete(
            @AuthenticationPrincipal User loginUser
    ){
        return ResponseEntity.ok(MessageUtils.success(userService.deleteUser(loginUser)));
    }

}
