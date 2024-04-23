package org.egglog.api.user.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.hospital.exception.HospitalErrorCode;
import org.egglog.api.hospital.exception.HospitalException;
import org.egglog.api.hospital.model.entity.Hospital;
import org.egglog.api.hospital.model.repository.HospitalJpaRepository;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.dto.request.UpdateUserHospitalRequest;
import org.egglog.api.user.model.dto.request.UpdateUserRequest;
import org.egglog.api.user.model.dto.response.UserResponse;
import org.egglog.api.user.model.entity.Users;
import org.egglog.api.user.repository.UserJpaRepository;
import org.egglog.api.user.repository.UserQueryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserJpaRepository userJpaRepository;
    private final UserQueryRepository userQueryRepository;
    private final HospitalJpaRepository hospitalJpaRepository;

    @Transactional(readOnly = true)
    public UserResponse findById(Long id){
        return userQueryRepository.findByIdWithHospital(id)
                .orElseThrow(()->new UserException(UserErrorCode.NOT_EXISTS_USER))
                .toResponse();
    }

    /**
     * 유저 정보 업데이트
     * @param loginUser 로그인 유저
     * @param request 유저이름, 유저 프로필 이미지 주소
     * @return 정보가 변경된 로그인 유저
     * @author 김형민
     */
    @Transactional
    public UserResponse updateUser(Users loginUser, UpdateUserRequest request){
        return userJpaRepository.save(loginUser.updateUserInfo(request.getUserName(), request.getProfileImgUrl()))
                .toResponse();
    }

    /**
     * 유저 병원 정보 업데이트
     * @param loginUser 로그인 유저
     * @param request 유저이름, 유저 프로필 이미지 주소
     * @return 정보가 변경된 로그인 정보
     * @author 김형민
     */
    @Transactional
    public UserResponse updateUserHospital(Users loginUser, UpdateUserHospitalRequest request){
        Hospital hospital = hospitalJpaRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new HospitalException(HospitalErrorCode.NOT_FOUND));
        return userJpaRepository.save(loginUser.updateHospitalInfo(hospital, request.getEmpNo()))
                .toResponse();
    }

    /**
     * 유저 정보 삭제
     * @param loginUser 로그인 유저
     * @return 정보가 변경된 로그인 정보
     * @author 김형민
     */
    @Transactional
    public UserResponse deleteUser(Users loginUser){
        return userJpaRepository.save(loginUser.deleteUser()).toResponse();
    }
}
