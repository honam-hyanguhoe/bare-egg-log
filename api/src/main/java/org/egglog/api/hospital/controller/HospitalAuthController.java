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
 * 병원, 간호사 인증 컨트롤러
 * @author 김형민
 */
@RestController
@RequestMapping("/v1/hospital-auth")
@RequiredArgsConstructor
@Slf4j
public class HospitalAuthController {

    private final HospitalAuthService hospitalAuthService;

    @PostMapping("/create")
    public ResponseEntity<MessageUtils> createAuth(
            @AuthenticationPrincipal User loginUser,
            @RequestBody CreateHospitalAuthRequest request
            ){
        return ResponseEntity.ok().body(
                MessageUtils.success(hospitalAuthService.createHospitalAuth(loginUser, request)));
    }
}
