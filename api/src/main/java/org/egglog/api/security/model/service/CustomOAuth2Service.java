package org.egglog.api.security.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.model.entity.OAuthAttribute;
import org.egglog.api.user.model.entity.enums.AuthProvider;
import org.egglog.api.user.model.entity.enums.UserRole;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.egglog.api.security.model.entity.OAuthAttribute.*;

@Service
@Transactional
@Slf4j
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        log.debug(" ==== ==== ==== [#oauth2.0 라이브러리 소셜 유저 정보 조회 서비스 실행] ==== ==== ====");
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest); //OAuth2 정보를 가져온다.

        Map<String, Object> originAttribute = oAuth2User.getAttributes(); // OAuth2User의 attribute

        // OAuth2 서비스 id (google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();//소셜 정보를 가져온다.

        OAuthAttribute oAuthAttribute = OAuthAttribute.of(registrationId, originAttribute);

        return new DefaultOAuth2User(Collections.singleton(()-> UserRole.GENERAL_USER.name()),oAuthAttribute.convertToMap(),"email");
    }

}
