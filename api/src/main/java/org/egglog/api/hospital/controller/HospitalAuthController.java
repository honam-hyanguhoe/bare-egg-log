package org.egglog.api.hospital.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.hospital.model.dto.request.CreateHospitalAuthRequest;
import org.egglog.api.hospital.model.dto.response.HospitalAuthListResponse;
import org.egglog.api.hospital.model.dto.response.HospitalAuthResponse;
import org.egglog.api.hospital.model.service.HospitalAuthService;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
 * |2024-05-02|김형민|주석 추가|
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
    public ResponseEntity<MessageUtils<HospitalAuthResponse>> createAuth(
            @AuthenticationPrincipal User loginUser,
            @RequestBody @Valid CreateHospitalAuthRequest request
            ){
        return ResponseEntity.ok().body(
                MessageUtils.success(hospitalAuthService.createHospitalAuth(loginUser, request)));
    }

    /**
     * 인증 요청 리스트 조회 API
     * @param adminUser 관리자 jwt 토큰
     * @param authType T : 인증된 리스트 F : 인증되지 않은 리스트
     * @author 김형민
     * @return
     */
    @GetMapping("/list/{authType}")
    public ResponseEntity<MessageUtils<List<HospitalAuthListResponse>>> findAuthList(
            @AuthenticationPrincipal User adminUser,
            @PathVariable Boolean authType

    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(hospitalAuthService.findHospitalAuthList(adminUser, authType)));
    }

    /**
     * 인증 하기 API
     * @param adminUser 관리자 jwt 토큰
     * @param authHospitalId 인증할 병원인증요청 id
     * @return
     */
    @PatchMapping("/cert/{authHospitalId}")
    public ResponseEntity<MessageUtils<HospitalAuthResponse>> cert(
            @AuthenticationPrincipal User adminUser,
            @PathVariable Long authHospitalId
    ){
        return ResponseEntity.ok().body(
                MessageUtils.success(hospitalAuthService.certHospitalAuth(adminUser, authHospitalId)));
    }
}
