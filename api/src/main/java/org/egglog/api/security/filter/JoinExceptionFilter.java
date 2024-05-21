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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class JoinExceptionFilter extends OncePerRequestFilter {
    private static final List<String> EXCLUDED_PATHS = Arrays.asList("/v1/hospital/list", "/v1/auth/login/**", "/v1/auth/refresh", "/test/**", "/swagger-ui/**", "/login/oauth2/code/**", "/oauth2/**", "/v3/api-docs/**", "/actuator/prometheus", "/actuator/metrics", "/v1/user/join/**");
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isExcluded = EXCLUDED_PATHS.stream().anyMatch(path -> pathMatcher.match(path, request.getRequestURI()));
        if (!isExcluded) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof User) {
                    User loginUser = (User) principal;
                    if (!loginUser.isJoin()) {
                        throw new AuthException(AuthErrorCode.PLZ_MORE_INFO);
                    }
                    if (loginUser.getUserStatus().equals(UserStatus.DELETED)) {
                        throw new AuthException(AuthErrorCode.DELETE_USER);
                    }
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
