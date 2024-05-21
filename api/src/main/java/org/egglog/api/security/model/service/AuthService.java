package org.egglog.api.security.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.config.OAuthProperties;
import org.egglog.api.security.model.dto.response.TokenResponse;
import org.egglog.api.security.model.entity.OAuthAttribute;
import org.egglog.api.user.exception.UserErrorCode;
import org.egglog.api.user.exception.UserException;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.model.entity.enums.AuthProvider;
import org.egglog.api.user.model.entity.enums.UserStatus;
import org.egglog.api.user.repository.jpa.UserJpaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final UserJpaRepository userJpaRepository;
    private final WebClient webClient;
    private final OAuthProperties oAuthProperties;
    public TokenResponse login(String accessToken, AuthProvider provider){
        log.debug(" ==== ==== ==== [login 서비스 실행] ==== ==== ====");
        Map<AuthProvider, String> clientKeyMap = Map.of(
                AuthProvider.GOOGLE, "google",
                AuthProvider.KAKAO, "kakao",
                AuthProvider.NAVER, "naver",
                AuthProvider.APPLE, "apple"
        );
        OAuthProperties.Client clientConfig = oAuthProperties.getRegistration().get(clientKeyMap.get(provider));
        OAuthProperties.Details detailsConfig = oAuthProperties.getProvider().get(clientKeyMap.get(provider));


        if (provider.equals(AuthProvider.GOOGLE)){
            Map tokenResponse = webClient.post()
                    .uri(detailsConfig.getTokenUri())
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(Mono.just(buildFormData(clientConfig, accessToken)), String.class)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            accessToken = (String) tokenResponse.get("access_token");
        }


        Map<String, Object> userAttributes = webClient.get()
                .uri(detailsConfig.getUserInfoUri())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        log.debug("userAttributes={}",userAttributes.toString());
        if (userAttributes == null || userAttributes.isEmpty()) throw new OAuth2AuthenticationException("해당 소셜 계정에 유저 정보가 없습니다.");


        OAuthAttribute oAuthAttribute = OAuthAttribute.of(provider, userAttributes);
        User oauthUser = oAuthAttribute.toUser();
        Optional<User> optionalUser = userJpaRepository.findByEmailWithHospital(oauthUser.getEmail());
        if (optionalUser.isPresent()){
            //로그인인 경우
            User user = optionalUser.get();
            if (user.getUserStatus().equals(UserStatus.DELETED)) throw new UserException(UserErrorCode.DELETED_USER);
            if (user.getUserStatus().equals(UserStatus.INACTIVE)) throw new UserException(UserErrorCode.INACTIVE_USER);
            userJpaRepository.save(user.doLogin());
            return tokenService.generatedToken(user.getId(), user.getUserRole().name());
        }
        //회원가입일 경우
        User saveUser = userJpaRepository.save(oauthUser);
        return tokenService.generatedToken(saveUser.getId(), saveUser.getUserRole().name());
    }

    public void logout(User user){
        userJpaRepository.save(user.doLogout());
        tokenService.RemoveToken(user.getId());
    }

    public TokenResponse refresh(String refreshToken){
        return tokenService.republishToken(refreshToken);
    }

    private String buildFormData(OAuthProperties.Client clientConfig, String code) {
        return "client_id=" + clientConfig.getClientId() +
                "&client_secret=" + clientConfig.getClientSecret() +
                "&redirect_uri=" +clientConfig.getRedirectUri() +
                "&code=" + code +
                "&grant_type=authorization_code";
    }
}

