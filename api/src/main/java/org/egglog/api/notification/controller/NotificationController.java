package org.egglog.api.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.notification.model.dto.request.NotificationRequest;
import org.egglog.api.notification.model.dto.response.NotificationResponse;
import org.egglog.api.notification.model.service.NotificationService;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.MessageUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.notification.controller
 * fileName      : NotificationController
 * description    : 알림 관련 api 입니다.
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-13|김형민|최초 생성|
 */
@RestController
@RequestMapping("/v1/notification")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    /**
     * 알림 설정 리스트 조회
     * @param loginUser JWT 토큰
     * @return
     * @author 김형민
     */
    @GetMapping("/list")
    public ResponseEntity<MessageUtils<List<NotificationResponse>>> findNotificationList(
            @AuthenticationPrincipal User loginUser
            ){
        return ResponseEntity.ok().body(MessageUtils.success(notificationService.findNotificationList(loginUser)));
    }


    /**
     * 알림 설정 상태 변경
     * @param loginUser JWT 토큰
     * @return
     * @author 김형민
     */
    @PatchMapping("/update")
    public ResponseEntity<MessageUtils<List<NotificationResponse>>> setNotificationList(
            @AuthenticationPrincipal User loginUser,
            @RequestBody List<NotificationRequest> requests
    ){
        return ResponseEntity.ok().body(MessageUtils.success(notificationService.updateNotification(requests, loginUser)));
    }

}
