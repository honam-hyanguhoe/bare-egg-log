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

    @Transactional(readOnly = true)
    public UserResponse findById(Long id){
        return userJpaRepository.findByIdWithHospital(id)
                .orElseThrow(()->new UserException(UserErrorCode.NOT_EXISTS_USER))
                .toResponse();
    }
    @Transactional(readOnly = true)
    public UserResponse find(User loginUser){
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
        Hospital hospital = hospitalJpaRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new HospitalException(HospitalErrorCode.NOT_FOUND));
        //todo 1. 기본 태그 자동 생성
        workTypeJpaRepository.saveAll(getDefaultWorkTypes(loginUser));
        //todo 2. 근무 일정 캘린더 그룹 자동 생성
        CalendarGroup workGroup = calendarGroupRepository.save(CalendarGroup.builder().alias("근무 일정").user(loginUser).build());
        return userJpaRepository.save(loginUser.join(request.getUserName(), hospital, request.getEmpNo(), request.getFcmToken(), workGroup.getId()))
                .toResponse();
    }

    private List<WorkType> getDefaultWorkTypes(User loginUser) {
        List<WorkType> defaultWorkTypes = new ArrayList<>();
        defaultWorkTypes.add(WorkType.builder()
                .title("DAY")
                .color("#18C5B5")
                .workTag(WorkTag.DAY)
                .startTime(LocalTime.of(6,0))//오전 6시 ~ 오후 2시 | 오후 2시 ~ 10시 | 오후 10시 + 익일 오전 6시
                .workTime(LocalTime.of(8,0))//8시간
                .user(loginUser)
                .build());
        defaultWorkTypes.add(WorkType.builder()
                .title("EVE")
                .color("#F4D567")
                .workTag(WorkTag.EVE)
                .startTime(LocalTime.of(2,0))//오전 6시 ~ 오후 2시 | 오후 2시 ~ 10시 | 오후 10시 + 익일 오전 6시
                .workTime(LocalTime.of(8,0))//8시간
                .user(loginUser)
                .build());
        defaultWorkTypes.add(WorkType.builder()
                .title("NIGHT")
                .color("#E55555")
                .workTag(WorkTag.NIGHT)
                .startTime(LocalTime.of(2,0))//오전 6시 ~ 오후 2시 | 오후 2시 ~ 10시 | 오후 10시 + 익일 오전 6시
                .workTime(LocalTime.of(8,0))//8시간
                .user(loginUser)
                .build());
        defaultWorkTypes.add(WorkType.builder()
                .title("OFF")
                .color("#9B8AFB")
                .workTag(WorkTag.OFF)
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
        User updateUser = userJpaRepository.save(loginUser.updateInfo(request.getUserName(), request.getProfileImgUrl()));
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
        User updateUser = userJpaRepository.save(loginUser.updateFcmToken(request.getFcmToken()));
        Optional<HospitalAuth> hospitalAuth = hospitalAuthJpaRepository.findByUserAndHospital(updateUser, updateUser.getSelectedHospital());
        if (hospitalAuth.isPresent()){
            return updateUser.toResponse(hospitalAuth.get());
        }
        return updateUser.toResponse();
    }

    /**
     * 유저 병원 정보 업데이트
     * @param loginUser 로그인 유저
     * @param request 유저이름, 유저 프로필 이미지 주소
     * @return 정보가 변경된 로그인 정보
     * @author 김형민
     */
    @Transactional
    public UserResponse updateUserHospital(User loginUser, UpdateUserHospitalRequest request){
        Hospital selectHospital = hospitalJpaRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new HospitalException(HospitalErrorCode.NOT_FOUND));
        //유저 병원 업데이트
        User updateUser = userJpaRepository.save(loginUser.updateHospital(selectHospital, request.getEmpNo()));
        Optional<HospitalAuth> hospitalAuth = hospitalAuthJpaRepository.findByUserAndHospital(updateUser, selectHospital);
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
        return userJpaRepository.save(loginUser.delete())
                .toResponse();
    }
}
