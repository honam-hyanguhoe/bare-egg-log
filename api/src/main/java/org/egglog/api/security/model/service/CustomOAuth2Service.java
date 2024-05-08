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
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

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
        log.debug(" ==== ==== ==== [#oauth2.0 라이브러리 소셜 유저 정보 조회 서비스 실행] ==== ==== ====");
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest); //OAuth2 정보를 가져온다.

        Map<String, Object> originAttribute = oAuth2User.getAttributes(); // OAuth2User의 attribute

        // OAuth2 서비스 id (google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();//소셜 정보를 가져온다.

        OAuthAttribute oAuthAttribute = OAuthAttribute.of(registrationId, originAttribute);

        return new DefaultOAuth2User(Collections.singleton(()-> UserRole.GENERAL_USER.name()),oAuthAttribute.convertToMap(),"email");
    }



    public OAuth2User loadUserByAccessToken(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.debug(" ==== ==== ==== [#oauth2.0 커스텀 소셜 유저 정보 조회 서비스 실행] ==== ==== ====");
        String userInfoEndpointUri = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUri();
        String tokenValue = userRequest.getAccessToken().getTokenValue();
        try {
            Map<String, Object> userAttributes = webClient.get()
                    .uri(userInfoEndpointUri)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenValue)
                    .retrieve()
                    .onStatus(status -> status.isError(), response -> {
                        return response.bodyToMono(String.class).flatMap(errorMessage -> {
                            return Mono.error(new RuntimeException("Error from Server: " + errorMessage));
                        });
                    })
                    .bodyToMono(Map.class)
                    .block();

            log.debug("userAttributes={}",userAttributes.toString());
            if (userAttributes == null || userAttributes.isEmpty()) {
                throw new OAuth2AuthenticationException("User Info Endpoint did not return any information");
            }

            // OAuth2 서비스 id (google, kakao, naver)
            String registrationId = userRequest.getClientRegistration().getRegistrationId();//소셜 정보를 가져온다.

            OAuthAttribute oAuthAttribute = OAuthAttribute.of(registrationId, userAttributes);

            return new DefaultOAuth2User(Collections.singleton(()-> UserRole.GENERAL_USER.name()),oAuthAttribute.convertToMap(),"email");
        } catch (WebClientResponseException ex) {
            log.error("WebClient Response Error: Status = {}, Body = {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new OAuth2AuthenticationException("WebClient response error");
        } catch (Exception ex) {
            log.error("Unexpected error", ex);
            throw new OAuth2AuthenticationException("Unexpected error when retrieving user information");
        }
    }

}
