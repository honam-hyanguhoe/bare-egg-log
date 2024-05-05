package org.egglog.api.hospital.model.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.hospital.model.dto.request.FindHospitalListRequest;
import org.egglog.api.hospital.model.dto.response.HospitalListResponse;
import org.egglog.api.hospital.model.dto.response.UserHospitalResponse;
import org.egglog.api.hospital.model.entity.Hospital;
import org.egglog.api.hospital.repository.jpa.HospitalJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : org.egglog.api.hospital.model.service
 * fileName       : HospitalService
 * author         : 김형민
 * date           : 2024-05-06
 * description    : 병원 서비스 (리스트 조회, 검색)
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-06        김형민       최초 생성
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalService {
    private final HospitalJpaRepository hospitalJpaRepository;

    public HospitalListResponse findList(FindHospitalListRequest request){
        return HospitalListResponse.builder()
                .offset(request.getOffset())
                .limit(request.getLimit())
                .hospitals(hospitalJpaRepository
                        .findHospitals(request.getHospitalName(), request.getOffset(), request.getLimit())
                                .stream()
                                .map(Hospital::toUserHospitalResponse)
                                .collect(Collectors.toList()))
                .build();
    }


}
