package org.egglog.api.security.config;

import com.nursetest.app.security.filter.JwtFilter;
import com.nursetest.app.security.handler.OAuth2SuccessHandler;
import com.nursetest.app.security.model.service.CustomOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig{

    private static final String[] ALLOWED_URIS = {"/**"};
    private final JwtFilter jwtFilter;
    private final CustomOAuth2Service customOAuth2Service;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ALLOWED_URIS).permitAll() // 특정 경로 인증 미요구
                        .anyRequest().authenticated() // 나머지 경로는 인증 요구
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // JwtFilter 추가
                .oauth2Login(customizer ->
                    customizer.successHandler(oAuth2SuccessHandler)
                            .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2Service))
                );
        return http.build();
    }

}
