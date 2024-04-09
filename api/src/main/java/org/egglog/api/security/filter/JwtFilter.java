package org.egglog.api.security.filter;

import com.nursetest.app.security.util.JwtUtil;
import com.nursetest.app.user.exception.UserException;
import com.nursetest.app.user.model.dto.params.FindParam;
import com.nursetest.app.user.model.dto.response.UserDto;
import com.nursetest.app.user.model.entity.User;
import com.nursetest.app.user.model.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.nursetest.app.user.exception.UserErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);

        //accessToken 없는 경우는 토큰 검사하지 않는다. (로그인 하지 않은 경우)
        if (!StringUtils.hasText(authorizationHeader)){
            doFilter(request, response, filterChain);
            return;
        }
        // Bearer로 시작되지 않으면 잘못된 토큰
        if (!authorizationHeader.startsWith(BEARER_PREFIX)){
            doFilter(request, response, filterChain);
            return;
        }
        // 전송받은 값에서 'Bearer ' 뒷부분(Jwt Token) 추출
        String accessToken = authorizationHeader.split(" ")[1];

        //accessToken 토큰 검증
        Jws<Claims> claimsJws = jwtUtil.validateAccessToken(accessToken);
        if (claimsJws!=null){
            User user = userMapper.find(
                    FindParam.builder()
                            .id(jwtUtil.getUserIdByAccessToken(accessToken))
                            .build())
                    .orElseThrow(()->new UserException(NOT_EXISTS_USER));

            // securityContext에 등록할 User 객체를 만든다.
            UserDto userDto = UserDto.of(user);

            //todo : userDto를 security Context에 담는다.
            Authentication auth = new UsernamePasswordAuthenticationToken(userDto, null, userDto.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

}
