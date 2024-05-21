package org.egglog.api.hospital.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.hospital.exception.HospitalErrorCode;
import org.egglog.api.hospital.exception.HospitalException;
import org.egglog.api.hospital.model.dto.request.CreateHospitalAuthRequest;
import org.egglog.api.hospital.model.dto.response.HospitalAuthListResponse;
import org.egglog.api.hospital.model.dto.response.HospitalAuthResponse;
import org.egglog.api.hospital.model.entity.HospitalAuth;
import org.egglog.api.hospital.repository.jpa.HospitalAuthJpaRepository;
import org.egglog.api.notification.model.service.NotificationService;
import org.egglog.api.security.exception.JwtErrorCode;
import org.egglog.api.security.exception.JwtException;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.model.entity.enums.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
 * 2024-05-02        김형민       인증 확인, 요청 리스트 조회 추가
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalAuthService {

    private final HospitalAuthJpaRepository hospitalAuthJpaRepository;
    private final NotificationService notificationService;
    /**
     * 병원 인증 요청입니다. 요청 데이터가 존재했다면 상태 업데이트, 새 요청이라면 생성합니다.
     * @param loginUser 요청을 보내는 로그인 유저
     * @param request 간호사 인증 사진, 병원 인증 사진
     * @return 인증 요청 결과 데이터
     * @author 김형민
     */
    @Transactional
    public HospitalAuthResponse createHospitalAuth(User loginUser, CreateHospitalAuthRequest request){
        log.debug(" ==== ==== ==== [ 병원 인증 요청 생성 조회 서비스 실행 ] ==== ==== ====");
        return hospitalAuthJpaRepository.save(hospitalAuthJpaRepository
                .findByUserAndHospital(loginUser, loginUser.getSelectedHospital())
                .map(auth -> auth.create(loginUser, request.getNurseCertificationImgUrl(), request.getHospitalCertificationImgUrl()))
                .orElseGet(() -> new HospitalAuth().create(loginUser, request.getNurseCertificationImgUrl(), request.getHospitalCertificationImgUrl())))
                .toResponse();
    }

    @Transactional(readOnly = true)
    public List<HospitalAuthListResponse> findHospitalAuthList(User masterUser, Boolean authType){
        if (masterUser.getUserRole()!= UserRole.ADMIN) throw new UserException(UserErrorCode.ACCESS_DENIED);
        log.debug(" ==== ==== ==== [ [관리자] 요청된 병원 인증 리스트 조회 서비스 실행 ] ==== ==== ====");
        return hospitalAuthJpaRepository.findAuthListWithUser(authType).stream()
                .map(auth -> HospitalAuthListResponse.builder()
                        .userId(auth.getUser().getId())
                        .hospitalAuthId(auth.getId())
                        .userName(auth.getUser().getName())
                        .userEmail(auth.getUser().getEmail())
                        .userStatus(auth.getUser().getUserStatus())
                        .selectHospital(auth.getUser().getSelectedHospital().toUserHospitalResponse())
                        .hospitalAuth(auth.toResponse())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public HospitalAuthResponse certHospitalAuth(User adminUser, Long authHospitalId){
        if (adminUser.getUserRole()!= UserRole.ADMIN) throw new UserException(UserErrorCode.ACCESS_DENIED);
        log.debug(" ==== ==== ==== [ [관리자] 인증하기 서비스 실행 ] ==== ==== ====");
        HospitalAuth hospitalAuth = hospitalAuthJpaRepository.findByIdWithHospitalAndUser(authHospitalId)
                .orElseThrow(() -> new HospitalException(HospitalErrorCode.AUTH_NOT_FOUND));
        HospitalAuth confirm = hospitalAuth.confirm(adminUser);
        notificationService.certHospitalNotification(confirm);
        return hospitalAuthJpaRepository.save(confirm).toResponse();
    }

}
