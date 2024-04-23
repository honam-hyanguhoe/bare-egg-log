package org.egglog.api.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.util.JwtUtils;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.repository.UserJpaRepository;
import org.egglog.api.user.repository.UserQueryRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final UserJpaRepository userJpaRepository;
    private final UserQueryRepository userQueryRepository;
    private final JwtUtils jwtUtils;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        //토큰이 없는경우
        if(!StringUtils.hasText(authorizationHeader)){
            doFilter(request, response, filterChain);
            return;
        }
        //Bearer 시작하지 않는 경우
        if(!authorizationHeader.startsWith(BEARER_PREFIX)){
            doFilter(request, response, filterChain);
            return;
        }
        //jwt추출
        String accessToken = authorizationHeader.split(" ")[1];

        //엑세스 토큰 검증
        Jws<Claims> claimsJws = jwtUtils.validateAccessToken(accessToken);
        if(claimsJws != null){
            User user = userQueryRepository.findById(jwtUtils.getUserIdByAccessToken(accessToken))
                    .orElseThrow(() -> new UserException(UserErrorCode.NOT_EXISTS_USER));

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request, response);

    }

}
