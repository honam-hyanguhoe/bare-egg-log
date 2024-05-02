package org.egglog.api.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.exception.JwtErrorCode;
import org.egglog.api.security.exception.JwtException;
import org.egglog.api.security.model.service.TokenService;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.model.entity.enums.UserStatus;
import org.egglog.api.user.repository.jpa.UserJpaRepository;
import org.egglog.api.user.repository.jpa.UserQueryRepositoryImpl;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenService tokenService;
    private final UserJpaRepository userJpaRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        User oauth2user = User.of(oAuth2User);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        //회원 가입 일 경우
        Optional<User> optionalUser = userJpaRepository.findByEmailWithHospital(oauth2user.getEmail());
        if (optionalUser.isPresent()){
            //로그인인 경우
            User user = optionalUser.get();
            if (user.getUserStatus()!= UserStatus.ACTIVE) throw new UserException(UserErrorCode.DELETED_USER);
            userJpaRepository.save(user.doLogin());
            response.getWriter().write(objectMapper.writeValueAsString(
                    MessageUtils.success(tokenService.generatedToken(user.getId(), user.getUserRole().name()))
            ));
        }
        else {
            //회원가입일 경우
            User saveUser = userJpaRepository.save(oauth2user);
            response.getWriter().write(objectMapper.writeValueAsString(
                    MessageUtils.success(tokenService.generatedToken(saveUser.getId(), saveUser.getUserRole().name()))
            ));
        }

    }
}

