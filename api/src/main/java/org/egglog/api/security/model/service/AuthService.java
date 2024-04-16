//package org.egglog.api.security.model.service;
//
//import com.nursetest.app.security.exception.AuthException;
//import com.nursetest.app.security.model.dto.response.GeneratedToken;
//import com.nursetest.app.user.model.entity.User;
//import com.nursetest.app.user.model.service.UserService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import static com.nursetest.app.security.exception.AuthErrorCode.*;
//import static com.nursetest.app.user.model.entity.enums.UserStatus.*;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class AuthService {
//
//    private final UserService userService;
//    private final PasswordEncoder passwordEncoder;
//    private final TokenService tokenService;
//
//    //todo : 일반 로그인, 소셜 로그인 등의 인증 로직이 들어갑니다.
//
//    public GeneratedToken login(String email, String password){
//        User user = userService.find(email);
//        log.debug("email={}",email);
//        log.debug("login password={}", password);
//        log.debug("userpassword={}", user.getPassword());
//        if (user!=null && passwordEncoder.matches(password, user.getPassword())){
//            if (user.getStatus().equals(ACTIVE)){
//                GeneratedToken generatedToken = tokenService.generatedToken(user.getId(), user.getRole().name());
//                if (user.getUserName().equals("미기입")||user.getHospitalName().equals("미기입")||user.getEmpNo().equals("미기입")) {
//                    generatedToken.setStatus(1);
//                }
//                return generatedToken;
//            }
//            if (user.getStatus().equals(DELETED))throw new AuthException(DELETE_USER);
//            if (user.getStatus().equals(INACTIVE))throw new AuthException(INACTIVE_USER);
//        }
//        throw new AuthException(NOT_EXISTS);
//    }
//
//
//}
