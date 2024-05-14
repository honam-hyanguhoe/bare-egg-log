package org.egglog.api.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * ===================[Info]=========================
 * packageName    : org.egglog.api.security.config
 * fileName      : OAuthProperties
 * description    : Configuration properties for OAuth 2.0 client registrations and providers.
 * =================================================
 * |DATE|AUTHOR|NOTE|
 * |2024-05-09|김형민|최초 생성|
 */
@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client")
@Data
public class OAuthProperties {
    private Map<String, Client> registration;
    private Map<String, Details> provider;


    @Data
    public static class Client {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String authorizationGrantType;
        private String clientAuthenticationMethod;
        private String clientName;
        private List<String> scope;
    }

    @Data
    public static class Details {
        private String authorizationUri;
        private String tokenUri;
        private String userInfoUri;
        private String userNameAttribute;
    }

}
