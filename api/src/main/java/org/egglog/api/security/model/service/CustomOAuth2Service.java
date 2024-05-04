package org.egglog.api.security.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.security.model.entity.OAuthAttribute;
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

import java.util.Collections;
import java.util.Map;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final WebClient webClient;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        log.debug("OAuth2UserRequest = {}", userRequest);

        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest); //OAuth2 정보를 가져온다.

        Map<String, Object> originAttribute = oAuth2User.getAttributes(); // OAuth2User의 attribute

        // OAuth2 서비스 id (google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();//소셜 정보를 가져온다.

        OAuthAttribute oAuthAttribute = OAuthAttribute.of(registrationId, originAttribute);

        return new DefaultOAuth2User(Collections.singleton(()-> UserRole.GENERAL_USER.name()),oAuthAttribute.convertToMap(),"email");
    }



    public OAuth2User loadUserByAccessToken(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String userInfoEndpointUri = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();
        String tokenValue = userRequest.getAccessToken().getTokenValue();

        Map<String, Object> userAttributes = webClient.get()
                .uri(userInfoEndpointUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (userAttributes == null || userAttributes.isEmpty()) {
            throw new OAuth2AuthenticationException("User Info Endpoint did not return any information");
        }

        // OAuth2 서비스 id (google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();//소셜 정보를 가져온다.

        OAuthAttribute oAuthAttribute = OAuthAttribute.of(registrationId, userAttributes);

        return new DefaultOAuth2User(Collections.singleton(()-> UserRole.GENERAL_USER.name()),oAuthAttribute.convertToMap(),"email");

    }

}
