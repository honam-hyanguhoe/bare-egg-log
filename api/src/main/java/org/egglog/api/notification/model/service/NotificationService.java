package org.egglog.api.notification.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.group.model.dto.response.GroupPreviewDto;
import org.egglog.api.group.repository.jpa.GroupRepository;
import org.egglog.api.notification.model.dto.request.NotificationRequest;
import org.egglog.api.notification.model.dto.response.NotificationResponse;
import org.egglog.api.notification.model.entity.FCMTopic;
import org.egglog.api.notification.model.entity.UserNotification;
import org.egglog.api.notification.model.entity.enums.TopicEnum;
import org.egglog.api.notification.repository.jpa.NotificationRepository;
import org.egglog.api.user.model.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.notification.model.service
 * fileName      : NotificationService
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-14|김형민|최초 생성|
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final GroupRepository groupRepository;
    private final FCMService fcmService;
    //설정 생성
    @Transactional
    public void makeDefaultNotification(User loginUser){
        List<UserNotification> list = new ArrayList<>();
        for (TopicEnum topicEnum : TopicEnum.values()) {
            list.add(UserNotification.builder()
                    .type(topicEnum)
                    .user(loginUser)
                    .status(false)
                    .updatedAt(LocalDateTime.now())
                    .build());
        }
        notificationRepository.saveAll(list);
    }

//    상태 변경
    @Transactional
    public List<NotificationResponse> updateNotification (List<NotificationRequest> requests, User loginUser){
        //todo : 상태변경 후 해당 구독 취소, 구독 을 하는 로직 필요 x -> 구독 유지후 로직으로 알림 발송 하는 방식으로 변경
        Map<Long, UserNotification> notificationMap = notificationRepository.findMapByUser(loginUser);
        List<UserNotification> list = new ArrayList<>();

        for (NotificationRequest request : requests) {
            list.add(notificationMap.get(request.getNotificationId()).updateState(request.getStatus()));
        }

        return notificationRepository.saveAll(list)
                .stream()
                .map(UserNotification::toResponse)
                .collect(Collectors.toList());
    }

    //현재 상태 조회
    @Transactional
    public List<NotificationResponse> findNotificationList(User loginUser){
        return notificationRepository.findListByUser(loginUser)
                .stream()
                .map(UserNotification::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateTopicSubscribeState(String oldToken, User loginUser){
        //현재 토픽 알림은 그룹 밖에 없다. 나중에 수정 필요.... 전체 토픽으로 설정된 값을 재 구독하는 방식으로..
        List<GroupPreviewDto> groupByUserId = groupRepository.findGroupByUserId(loginUser.getId());
        String newToken = loginUser.getDeviceToken();
        for (GroupPreviewDto groupPreviewDto : groupByUserId) {
            FCMTopic topic = FCMTopic.builder()
                    .topic(TopicEnum.GROUP)
                    .topicId(groupPreviewDto.getGroupId())
                    .build();
            fcmService.unsubscribeFromTopic(oldToken, topic); //예전 토큰 구독 취소
            fcmService.subscribeToTopic(newToken, topic); //새 토큰으로 구독
        }
    }

}
