package org.egglog.api.security.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.egglog.api.user.model.entity.enums.AuthProvider;

import java.util.HashMap;
import java.util.Map;

import static org.egglog.api.user.model.entity.enums.AuthProvider.*;

@Getter
@ToString
@Builder
public class OAuthAttribute {

    private Map<String, Object> attributes;
    private String nameAttributesKey;
    private String name;
    private String email;
    private String nickName;
    private String profileImgUrl;
    private String gender;
    private String birthdateYear;
    private String birthdateMonth;
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
                .nickName(String.valueOf(response.get("nickname")))
                .gender(String.valueOf(response.get("gender")))
                .birthdateYear(String.valueOf(response.get("birthyear")))
                .birthdateMonth(String.valueOf(response.get("birthday")))
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
                .name(String.valueOf(kakaoAccount.get("name")))
                .email(String.valueOf(kakaoAccount.get("email")))
                .birthdateMonth(String.valueOf(kakaoAccount.get("birthday")))
                .birthdateYear(String.valueOf(kakaoAccount.get("birthyear")))
                .gender(String.valueOf(kakaoAccount.get("gender")))
                .profileImgUrl(String.valueOf(kakaoProfile.get("profile_image_url")))
                .nickName(String.valueOf(kakaoProfile.get("nickname")))
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
        map.put("nickName",nickName);
        map.put("gender",gender);
        map.put("birthDateYear", birthdateYear);
        map.put("birthDateMonth", birthdateMonth);
        map.put("provider", provider);
        map.put("email", email);
        map.put("picture", profileImgUrl);
        return map;
    }


}
