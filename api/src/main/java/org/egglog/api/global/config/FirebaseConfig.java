package org.egglog.api.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.global.util.FirebaseProperties;
import org.egglog.api.notification.exception.NotificationErrorCode;
import org.egglog.api.notification.exception.NotificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FirebaseConfig {
    // 비밀키 경로 환경 변수
//    @Value("${fcm.config}") private String projectConfig;
    // 프로젝트 아이디 환경 변수
    @Value("${fcm.bucket-name}") private String bucketName;
    // 비밀키 경로 환경 변수
    private final FirebaseProperties firebaseProperties;
    // 의존성 주입이 이루어진 후 초기화를 수행한다.
    @PostConstruct
    public void initialize() {
        FirebaseOptions options = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream credentialsStream = new ByteArrayInputStream(mapper.writeValueAsString(firebaseProperties.getConfig()).getBytes());
            options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .setStorageBucket(bucketName)
                    .build();
        } catch (IOException e) {
            throw new NotificationException(NotificationErrorCode.NOTIFICATION_SERVER_ERROR);
        }
        if(FirebaseApp.getApps().isEmpty()){
            FirebaseApp.initializeApp(options);
        }
    }


    @Bean
    public FirebaseAuth firebaseAuth() throws IOException{
        return FirebaseAuth.getInstance();
    }

    @Bean
    public Bucket bucket() throws IOException {
        return StorageClient.getInstance().bucket();
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() {
        return FirebaseMessaging.getInstance();
    }

}
