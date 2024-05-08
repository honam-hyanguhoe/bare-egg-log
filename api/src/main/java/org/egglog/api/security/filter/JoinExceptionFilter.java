package org.egglog.api.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.exception.AuthErrorCode;
import org.egglog.api.security.exception.AuthException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.model.entity.enums.UserStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.security.filter
 * fileName      : JoinExceptionFilter
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-08|김형민|최초 생성|
 */
@Slf4j
@Configuration
public class JoinExceptionFilter extends OncePerRequestFilter {
    private static final List<String> EXCLUDED_PATHS = Arrays.asList("/v1/auth/join");
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!EXCLUDED_PATHS.stream().anyMatch(request.getRequestURI()::startsWith)){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User loginUser = (User) authentication.getPrincipal();
            if (!loginUser.isJoin()) throw new AuthException(AuthErrorCode.PLZ_MORE_INFO);
            if (loginUser.getUserStatus().equals(UserStatus.DELETED)) throw new AuthException(AuthErrorCode.DELETE_USER);
        }
        filterChain.doFilter(request, response);
    }
}
