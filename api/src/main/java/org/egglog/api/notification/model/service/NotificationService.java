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
        //todo : 상태변경 후 해당 구독 취소, 구독 을 하는 로직 필요
        Map<Long, UserNotification> notificationMap = notificationRepository.findMapByUser(loginUser);
        //현재 토픽 알림은 group 만 존재
        List<GroupPreviewDto> groupByUserId = groupRepository.findGroupByUserId(loginUser.getId());
        List<UserNotification> list = new ArrayList<>();
        for (NotificationRequest request : requests) {
            UserNotification userNotification = notificationMap.get(request.getNotificationId());
            if (userNotification.getStatus().equals(true)){
                //구독
                if (userNotification.getStatus().equals(TopicEnum.GROUP)){
                    //그룹이면 유저의 모든 그룹의 구독을 추가한다.
                    for (GroupPreviewDto groupPreviewDto : groupByUserId) {
                        String loginUserDeviceToken = loginUser.getDeviceToken();
                        FCMTopic topic = FCMTopic.builder()
                                .topic(TopicEnum.GROUP)
                                .topicId(groupPreviewDto.getGroupId())
                                .build();
                        if (loginUserDeviceToken!=null){
                            fcmService.subscribeToTopic(loginUserDeviceToken, topic);
                            list.add(userNotification.updateState(request.getStatus()));
                        }else {
                            list.add(userNotification);
                        }
                    }
                    continue;
                }
                list.add(userNotification.updateState(request.getStatus()));
            }else {
                //구독 취소
                if (userNotification.getStatus().equals(TopicEnum.GROUP)){
                    //그룹이면 유저의 모든 그룹의 구독을 취소한다.
                    for (GroupPreviewDto groupPreviewDto : groupByUserId) {
                        String loginUserDeviceToken = loginUser.getDeviceToken();
                        FCMTopic topic = FCMTopic.builder()
                                .topic(TopicEnum.GROUP)
                                .topicId(groupPreviewDto.getGroupId())
                                .build();
                        if (loginUserDeviceToken!=null){
                            fcmService.unsubscribeFromTopic(loginUserDeviceToken, topic);
                            list.add(userNotification.updateState(request.getStatus()));
                        }else {
                            list.add(userNotification);
                        }
                    }
                    continue;
                }
                list.add(userNotification.updateState(request.getStatus()));
            }
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


}
