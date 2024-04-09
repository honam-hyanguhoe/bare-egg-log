package org.egglog.api.fcm.model.service;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.fcm.exception.FcmErrorCode;
import org.egglog.api.fcm.exception.FcmException;
import org.egglog.api.fcm.model.mapper.FcmMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService {
    private final FcmMapper fcmMapper;

    // 비밀키 경로 환경 변수
    @Value("${fcm.service-account-file}") private String serviceAccountFilePath;
    // 프로젝트 아이디 환경 변수
    @Value("${fcm.project-id}") private String projectId;

    // 의존성 주입이 이루어진 후 초기화를 수행한다.
    @PostConstruct
    public void initialize() {
        FirebaseOptions options = null;
        try {
            options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(serviceAccountFilePath).getInputStream()))
                .setProjectId(projectId)
                .build();
        } catch (IOException e) {
            throw new RuntimeException("FCM 서버 세팅 중 에러가 발생했습니다: " + e);
        }

        FirebaseApp.initializeApp(options);
    }

    @Override
    public void subscribeMyTokens(Long userId, Long groupId) {
        List<String> deviceTokenList = fcmMapper.selectMyToken(userId);
        if(deviceTokenList.isEmpty() || deviceTokenList == null) throw new FcmException(FcmErrorCode.NO_EXIST_TOKEN);
        for(String deviceToken : deviceTokenList) subscribeByTopic(deviceToken, String.valueOf(groupId));
    }

    // 1. 로그인 시 새로운 디바이스 토큰이라면, 그룹아이디(topicName)에 token을 연결해줄 때 사용한다.
    // 2. 그룹에 가입될 때 사용한다.
    @Override
    public void subscribeByTopic(String token, String topicName) {
        try {
            FirebaseMessaging.getInstance().subscribeToTopic(Collections.singletonList(token), topicName);
        } catch (FirebaseMessagingException e) {
            throw new FcmException(FcmErrorCode.SUBSCRIBE_FAIL);
        }
    }

    // 지정된 topic에 fcm를 보냄
    @Override
    public void sendMessageByTopic(String title, String body, String topicName) {
        try {
            FirebaseMessaging.getInstance().send(Message.builder()
                .setNotification(Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build())
                .setTopic(topicName)
                .build());
        } catch (FirebaseMessagingException e) {
            throw new FcmException(FcmErrorCode.CAN_NOT_SEND_NOTIFICATION);
        }
    }

    // 받은 token을 이용하여 fcm를 보냄
    @Override
    public void sendMessageByToken(String title, String body, String token) {
        try {
            FirebaseMessaging.getInstance().send(Message.builder()
                .setNotification(Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build())
                .setToken(token)
                .build());
        } catch (FirebaseMessagingException e) {
            throw new FcmException(FcmErrorCode.CAN_NOT_SEND_NOTIFICATION);
        }
    }

    // 모든 기기에 fcm를 보내는 메서드
    @Override
    public void sendMessageAll(String title, String body) {
        List<String> tokenList = fcmMapper.getAllTokens();
        if(tokenList == null || tokenList.isEmpty()) throw new FcmException((FcmErrorCode.NO_EXIST_TARGET));
        try {
            FirebaseMessaging.getInstance().sendMulticast(MulticastMessage.builder()
                .setNotification(Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build())
                .addAllTokens(tokenList)
                .build());
        } catch (FirebaseMessagingException e) {
            throw new FcmException(FcmErrorCode.CAN_NOT_SEND_NOTIFICATION);
        }
    }
}