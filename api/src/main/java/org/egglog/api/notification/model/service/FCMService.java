package org.egglog.api.notification.model.service;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.notification.exception.NotificationErrorCode;
import org.egglog.api.notification.exception.NotificationException;
import org.egglog.api.notification.model.entity.FCMTopic;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.notification.model.service
 * fileName      : FCMService
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-13|김형민|최초 생성|
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;


    //토픽 구독
    public void subscribeToTopic(String token, FCMTopic fcmTopic) {
        try {
            log.debug("[FCM 서비스 실행] \n {} 토큰이 \n {} 토픽을 구독", token, fcmTopic.getTopic());
            firebaseMessaging.subscribeToTopic(Arrays.asList(token), fcmTopic.getTopic());
        } catch (FirebaseMessagingException e) {
            log.error("[FCM 구독 에러 발생] : {}", e.getMessage(), e);
            throw new NotificationException(NotificationErrorCode.NOTIFICATION_SERVER_ERROR);
        }
    }

    //토픽 구독 삭제
    public void unsubscribeFromTopic(String token, FCMTopic fcmTopic) {
        try {
            log.debug("[FCM 서비스 실행] \n {} 토큰이 \n {} 토픽을 구독 해제", token, fcmTopic.getTopic());
            firebaseMessaging.unsubscribeFromTopic(Arrays.asList(token), fcmTopic.getTopic());
        } catch (FirebaseMessagingException e) {
            log.error("[FCM 구독 해제 에러 발생] : {}", e.getMessage(), e);
            throw new NotificationException(NotificationErrorCode.NOTIFICATION_SERVER_ERROR);
        }
    }

    //토픽 알림
    public void sendNotificationToTopic(FCMTopic fcmTopic, Notification notification) {
        //todo : 딥링크 연결시 여기서 설정 또한 작성자는 알림 대상 제외 설정도 필요함 condition에서 설정
        Message message = Message.builder()
                .setNotification(notification)
                .setTopic(fcmTopic.getTopic())
                .build();
        try {
            log.debug("[FCM 서비스 실행] \n  {} 토픽 구독자 알림 발송", fcmTopic.getTopic());
            String response = firebaseMessaging.send(message);
            log.debug("[FCM 서비스 실행] \n  {} 토픽 구독자 알림 발송 성공", response);
        } catch (FirebaseMessagingException e) {
            log.error("[토픽 구독자 알림 발송 에러 발생] : {}", e.getMessage(), e);
            throw new NotificationException(NotificationErrorCode.NOTIFICATION_SERVER_ERROR);
        }
    }
    public void sendNotificationToTopic(FCMTopic fcmTopic, Notification notification, Map<String, String> data) {
        //todo : 딥링크 연결시 여기서 설정 또한 작성자는 알림 대상 제외 설정도 필요함 condition에서 설정
        Message message = Message.builder()
                .setNotification(notification)
                .putAllData(data)
                .setTopic(fcmTopic.getTopic())
                .build();
        try {
            log.debug("[FCM 서비스 실행] \n  {} 토픽 구독자 알림 발송", fcmTopic.getTopic());
            String response = firebaseMessaging.send(message);
            log.debug("[FCM 서비스 실행] \n  {} 토픽 구독자 알림 발송 성공", response);
        } catch (FirebaseMessagingException e) {
            log.error("[토픽 구독자 알림 발송 에러 발생] : {}", e.getMessage(), e);
            throw new NotificationException(NotificationErrorCode.NOTIFICATION_SERVER_ERROR);
        }
    }
    //개인 알림
    public void sendPersonalNotification(String token, Notification notification) {
        Message message = Message.builder()
                .setNotification(notification)
                .setToken(token)
                .build();
        try {
            log.debug("[FCM 서비스 실행] \n  {} 토큰에게 알림 발송", token);
            String response = firebaseMessaging.send(message);
            log.debug("[FCM 서비스 실행] \n  {} 알림 발송 성공", response);
        } catch (FirebaseMessagingException e) {
            log.error("[특정 토큰 알림 발송 에러 발생] : {}", e.getMessage(), e);
            throw new NotificationException(NotificationErrorCode.NOTIFICATION_SERVER_ERROR);
        }
    }
    public void sendPersonalNotification(String token, Notification notification, Map<String, String> data) {
        Message message = Message.builder()
                .setNotification(notification)
                .putAllData(data)
                .setToken(token)
                .build();
        try {
            log.debug("[FCM 서비스 실행] \n  {} 토큰에게 알림 발송", token);
            String response = firebaseMessaging.send(message);
            log.debug("[FCM 서비스 실행] \n  {} 알림 발송 성공", response);
        } catch (FirebaseMessagingException e) {
            log.error("[특정 토큰 알림 발송 에러 발생] : {}", e.getMessage(), e);
            throw new NotificationException(NotificationErrorCode.NOTIFICATION_SERVER_ERROR);
        }
    }

}
