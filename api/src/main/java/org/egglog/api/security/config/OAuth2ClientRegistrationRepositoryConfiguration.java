package org.egglog.api.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.ClientsConfiguredCondition;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.ArrayList;
import java.util.List;

/**
 * OAuth2 클라이언트 등록 구성을 위한 클래스
 */
@Configuration
@EnableConfigurationProperties(OAuth2ClientProperties.class)
@Conditional(ClientsConfiguredCondition.class)
@RequiredArgsConstructor
public class OAuth2ClientRegistrationRepositoryConfiguration {
    private final OAuth2ClientProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public InMemoryClientRegistrationRepository clientRegistrationRepository() {
        List<ClientRegistration> registrations = new ArrayList<>();
        // 네이버와 카카오 클라이언트만 등록
        properties.getRegistration().forEach((key, registration) -> {
            if (key.equals("naver") || key.equals("kakao")) {
                registrations.add(buildClientRegistration(key, registration));
            }
        });
        return new InMemoryClientRegistrationRepository(registrations);
    }

    private ClientRegistration buildClientRegistration(String registrationId, OAuth2ClientProperties.Registration registration) {
        OAuth2ClientProperties.Provider provider = properties.getProvider().get(registrationId);

        return ClientRegistration.withRegistrationId(registrationId)
                .clientId(registration.getClientId())
                .clientSecret(registration.getClientSecret())
                .redirectUri(registration.getRedirectUri())
                .authorizationGrantType(new AuthorizationGrantType(registration.getAuthorizationGrantType()))
                .clientName(registration.getClientName())
                .scope(registration.getScope().toArray(new String[0]))
                .authorizationUri(provider.getAuthorizationUri())
                .tokenUri(provider.getTokenUri())
                .userInfoUri(provider.getUserInfoUri()) // 이전에는 properties.getProvider().get(registrationId).getUserInfoEndpoint().getUri()
                .userNameAttributeName(provider.getUserNameAttribute()) // 이전에는 properties.getProvider().get(registrationId).getUserInfoEndpoint().getUserNameAttributeName()
                .build();
    }

}
