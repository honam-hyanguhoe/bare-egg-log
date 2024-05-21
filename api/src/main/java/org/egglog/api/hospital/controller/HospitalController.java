package org.egglog.api.hospital.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.hospital.model.dto.request.FindHospitalListRequest;
import org.egglog.api.hospital.model.dto.response.UserHospitalResponse;
import org.egglog.api.hospital.model.service.HospitalService;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.hospital.cotroller
 * fileName       : HospitalController
 * description    : 병원 컨트롤러
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-06|김형민|최초 생성|
 */
@RestController
@RequestMapping("/v1/hospital")
@RequiredArgsConstructor
@Slf4j
public class HospitalController {

    private final HospitalService hospitalService;

    /**
     * 병원 리스트 조회
     * @param request 검색어 필수 x, 시작 인덱스, 항목 수
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<MessageUtils<List<UserHospitalResponse>>> findHospitalList(
            @Valid @ModelAttribute FindHospitalListRequest request
            ){
        return ResponseEntity.ok().body(MessageUtils.success(hospitalService.findList(request)));
    }
}
