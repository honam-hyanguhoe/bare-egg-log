package org.egglog.api.security.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.model.dto.response.TokenResponse;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.model.entity.enums.AuthProvider;
import org.egglog.api.user.model.entity.enums.UserStatus;
import org.egglog.api.user.repository.jpa.UserJpaRepository;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * packageName    : org.egglog.api.security.model.service
 * fileName       : AuthService
 * author         : 김형민
 * date           : 2024-04-24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-24        김형민       최초 생성
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {
    private final TokenService tokenService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final CustomOAuth2Service customOAuth2Service;
    private final UserJpaRepository userJpaRepository;
    public TokenResponse login(String accessToken, AuthProvider provider){
        String registrationId = provider.name().toLowerCase(); // AuthProvider enum에서 registrationId를 도출
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(registrationId);
        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, null, null);
        OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, oAuth2AccessToken);
        OAuth2User oAuth2User = customOAuth2Service.loadUser(userRequest); // 사용자 정보 로드
        User oauth2user = User.of(oAuth2User);
        //회원 가입 일 경우
        Optional<User> optionalUser = userJpaRepository.findByEmailWithHospital(oauth2user.getEmail());
        if (optionalUser.isPresent()){
            //로그인인 경우
            User user = optionalUser.get();
            if (user.getUserStatus()!= UserStatus.ACTIVE) throw new UserException(UserErrorCode.DELETED_USER);
            userJpaRepository.save(user.doLogin());
            return tokenService.generatedToken(user.getId(), user.getUserRole().name());
        }
        else {
            //회원가입일 경우
            User saveUser = userJpaRepository.save(oauth2user);
            return tokenService.generatedToken(saveUser.getId(), saveUser.getUserRole().name());
        }
    }

    public void logout(User user){
        tokenService.RemoveToken(user.getId());
    }

    public TokenResponse refresh(String refreshToken){
        return tokenService.republishToken(refreshToken);
    }

}

