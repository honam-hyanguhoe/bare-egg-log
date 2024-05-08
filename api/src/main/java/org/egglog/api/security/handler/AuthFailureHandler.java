package org.egglog.api.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AuthFailureHandler implements AuthenticationFailureHandler, AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.debug(" ==== ==== ==== [#AuthFailureHandler onAuthenticationFailure 실행] ==== ==== ====");
        response.setStatus(401);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        // MessageUtils 객체를 사용하여 실패 응답 생성
        MessageUtils<?> failureResponse = MessageUtils.fail(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "소셜 로그인 실패");

        // ObjectMapper를 사용하여 MessageUtils 객체를 JSON 문자열로 변환
        String jsonResponse = objectMapper.writeValueAsString(failureResponse);

        // JSON 응답을 클라이언트에 전송
        response.getWriter().write(jsonResponse);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.debug(" ==== ==== ==== [#AuthFailureHandler commence 실행] ==== ==== ====");
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");

        // MessageUtils 객체를 사용하여 실패 응답 생성
        MessageUtils<?> failureResponse = MessageUtils.fail(HttpStatus.UNAUTHORIZED.toString(), "로그인이 필요합니다.");

        // ObjectMapper를 사용하여 MessageUtils 객체를 JSON 문자열로 변환
        String jsonResponse = objectMapper.writeValueAsString(failureResponse);

        // JSON 응답을 클라이언트에 전송
        response.getWriter().write(jsonResponse);
    }
}

