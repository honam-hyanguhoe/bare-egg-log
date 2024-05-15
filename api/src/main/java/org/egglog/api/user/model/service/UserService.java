package org.egglog.api.user.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.calendargroup.repository.jpa.CalendarGroupRepository;
import org.egglog.api.hospital.exception.HospitalErrorCode;
import org.egglog.api.hospital.exception.HospitalException;
import org.egglog.api.hospital.model.entity.Hospital;
import org.egglog.api.hospital.model.entity.HospitalAuth;
import org.egglog.api.hospital.repository.jpa.HospitalAuthJpaRepository;
import org.egglog.api.hospital.repository.jpa.HospitalAuthQueryRepositoryImpl;
import org.egglog.api.hospital.repository.jpa.HospitalJpaRepository;
import org.egglog.api.notification.model.service.NotificationService;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.dto.request.JoinUserRequest;
import org.egglog.api.user.model.dto.request.UpdateFcmRequest;
import org.egglog.api.user.model.dto.request.UpdateUserHospitalRequest;
import org.egglog.api.user.model.dto.request.UpdateUserRequest;
import org.egglog.api.user.model.dto.response.UserResponse;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.repository.jpa.UserJpaRepository;
import org.egglog.api.worktype.model.entity.WorkTag;
import org.egglog.api.worktype.model.entity.WorkType;
import org.egglog.api.worktype.repository.jpa.WorkTypeJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserJpaRepository userJpaRepository;
    private final HospitalJpaRepository hospitalJpaRepository;
    private final HospitalAuthJpaRepository hospitalAuthJpaRepository;
    private final WorkTypeJpaRepository workTypeJpaRepository;
    private final CalendarGroupRepository calendarGroupRepository;
    private final NotificationService notificationService;

    @Transactional(readOnly = true)
    public UserResponse findById(Long id){
        return userJpaRepository.findByIdWithHospital(id)
                .orElseThrow(()->new UserException(UserErrorCode.NOT_EXISTS_USER))
                .toResponse();
    }
    @Transactional(readOnly = true)
    public UserResponse find(User loginUser){
        log.debug(" ==== ==== ==== [ 유저 조회 서비스 실행] ==== ==== ====");
        Optional<HospitalAuth> hospitalAuth = hospitalAuthJpaRepository.findByUserAndHospital(loginUser, loginUser.getSelectedHospital());
        if (hospitalAuth.isPresent()){
            return loginUser.toResponse(hospitalAuth.get());
        }
        return loginUser.toResponse();
    }

    /**
     * 유저 회원 가입
     * @param loginUser 로그인 유저
     * @param request 유저이름, 병원, 사번
     * @return 최초 로그인한 로그인 유저
     * @author 김형민
     */
    @Transactional
    public UserResponse joinUser(User loginUser, JoinUserRequest request){
        log.debug(" ==== ==== ==== [ 회원가입 서비스 실행] ==== ==== ====");
        Hospital hospital = hospitalJpaRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new HospitalException(HospitalErrorCode.NOT_FOUND));
        //todo 1. 기본 태그 자동 생성
        workTypeJpaRepository.saveAll(getDefaultWorkTypes(loginUser));
        //todo 2. 근무 일정 캘린더 그룹 자동 생성
        CalendarGroup workGroup = calendarGroupRepository.save(CalendarGroup.builder().alias("[EGGLOG] 기본 캘린더").user(loginUser).build());
        //todo 3. 알림 설정 자동생성
        notificationService.makeDefaultNotification(loginUser);
        return userJpaRepository.save(loginUser.join(request.getUserName(), hospital, request.getEmpNo(), request.getFcmToken(), workGroup.getId()))
                .toResponse();
    }

    private List<WorkType> getDefaultWorkTypes(User loginUser) {
        List<WorkType> defaultWorkTypes = new ArrayList<>();
        defaultWorkTypes.add(WorkType.builder()
                .title("DAY")
                .color("#18C5B5")
                .workTag(WorkTag.DAY)
                .workTypeImgUrl("https://firebasestorage.googleapis.com/v0/b/egglog-422207.appspot.com/o/honam%2Feggs%2FDAY.png?alt=media&token=34852f61-9513-41ab-a733-f01c3014206a")
                .startTime(LocalTime.of(6,0))//오전 6시 ~ 오후 2시 | 오후 2시 ~ 10시 | 오후 10시 + 익일 오전 6시
                .workTime(LocalTime.of(8,0))//8시간
                .user(loginUser)
                .build());
        defaultWorkTypes.add(WorkType.builder()
                .title("EVE")
                .color("#F4D567")
                .workTag(WorkTag.EVE)
                .workTypeImgUrl("https://firebasestorage.googleapis.com/v0/b/egglog-422207.appspot.com/o/honam%2Feggs%2FEVE.png?alt=media&token=c62376bb-1ed2-45f0-83cd-d0aeeb19f4aa")
                .startTime(LocalTime.of(2,0))//오전 6시 ~ 오후 2시 | 오후 2시 ~ 10시 | 오후 10시 + 익일 오전 6시
                .workTime(LocalTime.of(8,0))//8시간
                .user(loginUser)
                .build());
        defaultWorkTypes.add(WorkType.builder()
                .title("NIGHT")
                .color("#E55555")
                .workTag(WorkTag.NIGHT)
                .workTypeImgUrl("https://firebasestorage.googleapis.com/v0/b/egglog-422207.appspot.com/o/honam%2Feggs%2FNIGHT.png?alt=media&token=1181cfa8-1e19-402b-a977-eb8d096a8576")
                .startTime(LocalTime.of(2,0))//오전 6시 ~ 오후 2시 | 오후 2시 ~ 10시 | 오후 10시 + 익일 오전 6시
                .workTime(LocalTime.of(8,0))//8시간
                .user(loginUser)
                .build());
        defaultWorkTypes.add(WorkType.builder()
                .title("OFF")
                .color("#9B8AFB")
                .workTag(WorkTag.OFF)
                .workTypeImgUrl("https://firebasestorage.googleapis.com/v0/b/egglog-422207.appspot.com/o/honam%2Feggs%2FOFF.png?alt=media&token=066f293c-9af2-4bdb-82d6-ba9a30a9b03d")
                .startTime(LocalTime.of(0,0))//오전 6시 ~ 오후 2시 | 오후 2시 ~ 10시 | 오후 10시 + 익일 오전 6시
                .workTime(LocalTime.of(1,0))//8시간
                .user(loginUser)
                .build());
        return defaultWorkTypes;
    }

    /**
     * 유저 정보 업데이트
     * @param loginUser 로그인 유저
     * @param request 유저이름, 유저 프로필 이미지 주소
     * @return 정보가 변경된 로그인 유저
     * @author 김형민
     */
    @Transactional
    public UserResponse updateUserInfo(User loginUser, UpdateUserRequest request){
        log.debug(" ==== ==== ==== [ 유저 회원 정보 수정 서비스 실행] ==== ==== ====");
        Hospital selectHospital = hospitalJpaRepository.findById(request.getSelectHospitalId())
                .orElseThrow(() -> new HospitalException(HospitalErrorCode.NOT_FOUND));
        User updateUser = userJpaRepository
                .save(loginUser.updateInfo(request.getUserName(), request.getProfileImgUrl(), selectHospital, request.getEmpNo()));
        Optional<HospitalAuth> hospitalAuth = hospitalAuthJpaRepository.findByUserAndHospital(updateUser, updateUser.getSelectedHospital());

        if (hospitalAuth.isPresent()){
            return updateUser.toResponse(hospitalAuth.get());
        }

        return updateUser.toResponse();
    }
    /**
     * 유저 정보 업데이트
     * @param loginUser 로그인 유저
     * @param request 변경할 FCM
     * @return 정보가 변경된 로그인 유저
     * @author 김형민
     */
    @Transactional
    public UserResponse updateFcmUser(User loginUser, UpdateFcmRequest request){
        log.debug(" ==== ==== ==== [ 유저 FCM 수정 서비스 실행] ==== ==== ====");
        String oldToken = loginUser.getDeviceToken();
        User updateUser = userJpaRepository.save(loginUser.updateFcmToken(request.getFcmToken()));
        Optional<HospitalAuth> hospitalAuth = hospitalAuthJpaRepository.findByUserAndHospital(updateUser, updateUser.getSelectedHospital());

        //todo 유저의 알림 리스트를 가져와서, 구독 취소 후 재 구독한다.
        notificationService.updateTopicSubscribeState(oldToken, updateUser);

        if (hospitalAuth.isPresent()){
            return updateUser.toResponse(hospitalAuth.get());
        }
        return updateUser.toResponse();
    }

    /**
     * 유저 정보 삭제
     * @param loginUser 로그인 유저
     * @return 정보가 변경된 로그인 정보
     * @author 김형민
     */
    @Transactional
    public UserResponse deleteUser(User loginUser){
        log.debug(" ==== ==== ==== [ 유저 탈퇴 서비스 실행] ==== ==== ====");
        return userJpaRepository.save(loginUser.delete())
                .toResponse();
    }
}
