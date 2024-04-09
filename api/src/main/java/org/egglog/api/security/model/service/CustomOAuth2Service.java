package org.egglog.api.security.model.service;

import com.nursetest.app.security.model.entity.OAuthAttribute;
import com.nursetest.app.user.model.entity.enums.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest); //OAuth2 정보를 가져온다.

        Map<String, Object> originAttribute = oAuth2User.getAttributes(); // OAuth2User의 attribute

        // OAuth2 서비스 id (google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();//소셜 정보를 가져온다.

        //OAuthAttributes: OAuth2User의 attribute를 auth provider 유형에 맞게 담아준다.
        OAuthAttribute oAuthAttribute = OAuthAttribute.of(registrationId, originAttribute);

        //가입이 되어 있는 회원이면 프로필 사진 업데이트, 회원가입이면
//        userMapper.find(FindParam.builder().email(oAuthAttribute.getEmail()).build())
//                .or
        return new DefaultOAuth2User(Collections.singleton(()->UserRole.GENERAL_USER.name()),oAuthAttribute.convertToMap(),"email");
    }
}
