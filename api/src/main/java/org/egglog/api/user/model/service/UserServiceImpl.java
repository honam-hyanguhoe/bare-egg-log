package org.egglog.api.user.model.service;

import com.nursetest.app.user.exception.UserErrorCode;
import com.nursetest.app.user.exception.UserException;
import com.nursetest.app.user.model.dto.params.FindParam;
import com.nursetest.app.user.model.dto.params.ModifyParam;
import com.nursetest.app.user.model.dto.request.EditUserRequest;
import com.nursetest.app.user.model.dto.request.FindEmailRequest;
import com.nursetest.app.user.model.dto.request.JoinUserRequest;
import com.nursetest.app.user.model.dto.request.RefreshPasswordRequest;
import com.nursetest.app.user.model.dto.response.UserDto;
import com.nursetest.app.user.model.entity.User;
import com.nursetest.app.user.model.entity.enums.UserStatus;
import com.nursetest.app.user.model.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.nursetest.app.user.exception.UserErrorCode.ALREADY_IN_EMAIL;
import static com.nursetest.app.user.exception.UserErrorCode.NOT_EXISTS_USER;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void join(JoinUserRequest joinUser) {
        log.debug("error={}",joinUser);
        userMapper.find(FindParam.builder().email(joinUser.getEmail()).build())
                .ifPresent(value ->{ throw new UserException(ALREADY_IN_EMAIL);});
        joinUser.setPassword(passwordEncoder.encode(joinUser.getPassword()));
        userMapper.join(joinUser);
    }

    @Override
    public User find(String email) {
        return userMapper.find(FindParam.builder().email(email).build()).orElseThrow(()-> new UserException(UserErrorCode.NOT_EXISTS_USER));
    }
    @Override
    public User find(Long id) {
        return userMapper.find(FindParam.builder().id(id).build()).orElseThrow(()-> new UserException(UserErrorCode.NOT_EXISTS_USER));
    }

    @Override
    public User findEmail(FindEmailRequest request) {
        return userMapper
                .find(FindParam.of(request))
                .orElseThrow(()->new UserException(NOT_EXISTS_USER));
    }

    @Override
    public void modify(Long userId, EditUserRequest editUser) {
        userMapper.modify(ModifyParam.requestToParam(userId, editUser));
    }

    @Override
    public void editPassword(Long userId, RefreshPasswordRequest refreshPasswordRequest) {
        userMapper.modify(ModifyParam.requestToParam(
                userId, RefreshPasswordRequest
                        .builder()
                        .newPassword(passwordEncoder.encode(refreshPasswordRequest.getNewPassword()))
                        .build()));
    }

    @Override
    public void modifyUserState(Long userId, UserStatus newState) {
        userMapper.modify(ModifyParam.builder().id(userId).status(newState).build());
    }

    @Override
    public void deleteUser(Long userId) {
        userMapper.modify(ModifyParam.builder()
                        .id(userId)
                        .email(UUID.randomUUID().toString())
                        .userName("탈퇴회원")
                        .password("null")
                        .hospitalName("default")
                        .empNo("default")
                        .profileImgUrl("null")
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return UserDto.of(find(Long.parseLong(id)));
    }
}
