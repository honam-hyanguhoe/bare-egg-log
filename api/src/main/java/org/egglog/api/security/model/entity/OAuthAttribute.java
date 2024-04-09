package org.egglog.api.security.model.entity;


import com.nursetest.app.user.model.entity.enums.AuthProvider;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

import static com.nursetest.app.user.model.entity.enums.AuthProvider.*;

@Getter
@ToString
@Builder
public class OAuthAttribute {

    private Map<String, Object> attributes; // Oauth2 서버에서 오는 유저 정보
    private String nameAttributesKey;
    private String name;
    private String email;
    private String profileImgUrl;
    private AuthProvider provider;

    public static OAuthAttribute of(String authProvider, Map<String, Object> attributes){
        if ("kakao".equals(authProvider)){
            return ofKakao("id", attributes);
        }else if ("google".equals(authProvider)){
            return ofGoogle("sub", attributes);
        }else if ("naver".equals(authProvider)){
            return ofNaver("id",attributes);
        }
        return null;
    }

    private static OAuthAttribute ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttribute.builder()
                .name(String.valueOf(response.get("name")))
                .email(String.valueOf(response.get("email")))
                .profileImgUrl(String.valueOf(response.get("profile_image")))
                .provider(NAVER)
                .attributes(response)
                .nameAttributesKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttribute ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttribute.builder()
                .name(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .profileImgUrl(String.valueOf(attributes.get("picture")))
                .provider(GOOGLE)
                .attributes(attributes)
                .nameAttributesKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttribute ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttribute.builder()
                .name(String.valueOf(kakaoProfile.get("nickname")))
                .email(String.valueOf(kakaoAccount.get("email")))
                .profileImgUrl(String.valueOf(kakaoProfile.get("profile_image_url")))
                .provider(KAKAO)
                .nameAttributesKey(userNameAttributeName)
                .attributes(attributes)
                .build();
    }
    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", nameAttributesKey);
        map.put("key", nameAttributesKey);
        map.put("name", name);
        map.put("provider", provider);
        map.put("email", email);
        map.put("picture", profileImgUrl);

        return map;
    }


}
