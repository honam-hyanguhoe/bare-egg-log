package org.egglog.api.hospital.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.hospital.model.dto.request.CreateHospitalAuthRequest;
import org.egglog.api.hospital.model.dto.response.HospitalAuthResponse;
import org.egglog.api.hospital.model.entity.HospitalAuth;
import org.egglog.api.hospital.repository.jpa.HospitalAuthJpaRepository;
import org.egglog.api.user.model.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName    : org.egglog.api.hospital.model.service
 * fileName       : HospitalAuthService
 * author         : 김형민
 * date           : 2024-04-26
 * description    : 병원 인증 서비스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-26        김형민       최초 생성
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalAuthService {

    private final HospitalAuthJpaRepository hospitalAuthJpaRepository;

    /**
     * 병원 인증 요청입니다. 요청 데이터가 존재했다면 상태 업데이트, 새 요청이라면 생성합니다.
     * @param loginUser 요청을 보내는 로그인 유저
     * @param request 간호사 인증 사진, 병원 인증 사진
     * @return 인증 요청 결과 데이터
     * @author 김형민
     */
    @Transactional
    public HospitalAuthResponse createHospitalAuth(User loginUser, CreateHospitalAuthRequest request){
        return hospitalAuthJpaRepository.save(hospitalAuthJpaRepository
                .findByUserAndHospital(loginUser, loginUser.getSelectedHospital())
                .map(auth -> auth.create(loginUser, request.getNurseCertificationImgUrl(), request.getHospitalCertificationImgUrl()))
                .orElseGet(() -> new HospitalAuth().create(loginUser, request.getNurseCertificationImgUrl(), request.getHospitalCertificationImgUrl())))
                .toResponse();
    }

}
