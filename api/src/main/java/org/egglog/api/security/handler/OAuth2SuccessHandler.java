//package org.egglog.api.security.handler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.nursetest.app.global.util.MessageUtils;
//import com.nursetest.app.security.exception.AuthException;
//import com.nursetest.app.security.model.dto.response.GeneratedToken;
//import com.nursetest.app.security.model.service.TokenService;
//import com.nursetest.app.user.model.dto.params.FindParam;
//import com.nursetest.app.user.model.dto.response.UserResponse;
//import com.nursetest.app.user.model.entity.User;
//import com.nursetest.app.user.model.entity.enums.UserStatus;
//import com.nursetest.app.user.model.mapper.UserMapper;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Optional;
//
//import static com.nursetest.app.security.exception.AuthErrorCode.DELETE_USER;
//import static com.nursetest.app.security.exception.AuthErrorCode.INACTIVE_USER;
//import static com.nursetest.app.user.model.entity.enums.UserStatus.DELETED;
//import static com.nursetest.app.user.model.entity.enums.UserStatus.INACTIVE;
//
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final TokenService tokenService;
//    private final UserMapper userMapper;
//    private final ObjectMapper objectMapper;
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        UserResponse userResponse = UserResponse.of(oAuth2User);
//        log.debug("userResponse={}", userResponse);
//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(HttpServletResponse.SC_OK);
//        //회원가입 일 경우: email, name, imgURL 리턴
//        //로그인일 경우(이미 존재하는 경우): JWT 토큰 리턴
//        Optional<User> optionalUser = userMapper.find(FindParam.builder().email(userResponse.getEmail()).build());
//        if (optionalUser.isPresent()){
//            //로그인일 경우
//            User user = optionalUser.get();
//            if (user.getStatus().equals(UserStatus.ACTIVE)){
//                log.debug("로그인 입니다.={}", user);
//                GeneratedToken generatedToken = tokenService.generatedToken(user.getId(), user.getRole().name());
//                if (user.getEmpNo().equals("미기입")||user.getUserName().equals("미기입")||user.getHospitalName().equals("미기입")){
//                    generatedToken.setStatus(1);
//                }
//                response.getWriter().write(objectMapper.writeValueAsString(
//                        MessageUtils.success(generatedToken)));
//            }
//            if (user.getStatus().equals(DELETED))throw new AuthException(DELETE_USER);
//            if (user.getStatus().equals(INACTIVE))throw new AuthException(INACTIVE_USER);
//        }else {
//            //회원가입 일 경우
//            response.getWriter().write(objectMapper.writeValueAsString(MessageUtils.success(userResponse)));
//        }
//    }
//}
