package org.egglog.api.hospital.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.hospital.model.dto.request.CreateHospitalAuthRequest;
import org.egglog.api.hospital.model.service.HospitalAuthService;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.hospital.cotroller
 * fileName       : HospitalAuthController
 * description    : 병원 인증 컨트롤러
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-26|김형민|최초 생성|
 * |2024-05-02|김형민|인증 확인, 요청 리스트 조회 추가|
 */
@RestController
@RequestMapping("/v1/hospital-auth")
@RequiredArgsConstructor
@Slf4j
public class HospitalAuthController {

    private final HospitalAuthService hospitalAuthService;

    /**
     * 병원 인증 요청 API
     * @param loginUser jwt 토큰
     * @param request 간호사 인증 이미지 URL, 재직 병원 인증 이미지 URL
     * @author 김형민
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<MessageUtils> createAuth(
            @AuthenticationPrincipal User loginUser,
            @RequestBody CreateHospitalAuthRequest request
            ){
        return ResponseEntity.ok().body(
                MessageUtils.success(hospitalAuthService.createHospitalAuth(loginUser, request)));
    }
}
