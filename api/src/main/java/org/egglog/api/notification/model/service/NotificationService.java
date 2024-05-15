package org.egglog.api.notification.model.service;

import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.board.exception.CommentErrorCode;
import org.egglog.api.board.exception.CommentException;
import org.egglog.api.board.model.dto.params.BoardForm;
import org.egglog.api.board.model.entity.Board;
import org.egglog.api.board.model.entity.BoardType;
import org.egglog.api.board.model.entity.Comment;
import org.egglog.api.board.repository.jpa.comment.CommentRepository;
import org.egglog.api.group.model.dto.response.GroupPreviewDto;
import org.egglog.api.group.model.entity.Group;
import org.egglog.api.group.model.entity.GroupMember;
import org.egglog.api.group.repository.jpa.GroupRepository;
import org.egglog.api.notification.exception.NotificationErrorCode;
import org.egglog.api.notification.exception.NotificationException;
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
    private final CommentRepository commentRepository;
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
        ArrayList<UserNotification> notificationList = new ArrayList<>();
        for (NotificationRequest request : requests) {
            UserNotification notification = notificationMap.get(request.getNotificationId());
            List<GroupPreviewDto> group = groupRepository.findGroupByUserId(loginUser.getId());
            if (request.getStatus()){
                //구독
                if (notification.getType().equals(TopicEnum.GROUP)){
                    //토픽 발송일때
                    for (GroupPreviewDto groupPreviewDto : group) {
                        FCMTopic topic = FCMTopic.builder()
                                .topic(TopicEnum.GROUP)
                                .topicId(groupPreviewDto.getGroupId())
                                .build();
                        fcmService.subscribeToTopic(loginUser.getDeviceToken(), topic);//구독
                    }
                }
            }else {
                // 구독 취소
                if (notification.getType().equals(TopicEnum.GROUP)){
                    //토픽 발송일때
                    for (GroupPreviewDto groupPreviewDto : group) {
                        FCMTopic topic = FCMTopic.builder()
                                .topic(TopicEnum.GROUP)
                                .topicId(groupPreviewDto.getGroupId())
                                .build();
                        fcmService.unsubscribeFromTopic(loginUser.getDeviceToken(), topic);//구독 취소
                    }
                }
            }
            notificationList.add(notification.updateState(request.getStatus()));
        }

        return notificationRepository.saveAll(notificationList)
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
        UserNotification groupNotification = notificationRepository.findByTypeAndUser(TopicEnum.GROUP, loginUser)
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_SERVER_ERROR));
        String newToken = loginUser.getDeviceToken();
        if (groupNotification.getStatus()){
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

    @Transactional
    public void groupMemberAcceptNotification(User user, Group group) {
        //1. 그룹에 새 멤버가 추가되었다면 해당 그룹 토픽으로 FCM 알림 발송
        FCMTopic topic = FCMTopic.builder()
                .topic(TopicEnum.GROUP)
                .topicId(group.getId())
                .build();
        Notification notification = Notification.builder()
                .setTitle("[EGGLOG]")
                .setBody(group.getGroupName()+"의 그룹에 새 멤버가 추가 되었습니다. 축하해주세요!")
                .build();
        fcmService.sendNotificationToTopic(topic, notification);

        //2. 그룹에 가입되었다면 해당 그룹 토픽 구독
        String loginUserDeviceToken = user.getDeviceToken();
        if (loginUserDeviceToken!=null){
            fcmService.subscribeToTopic(loginUserDeviceToken, topic);
        }
    }

    @Transactional
    public void deleteGroupMemberNotification(Long groupId, GroupMember member) {
        //해당 멤버가 삭제되었다면 해당 유저의 토픽 구독 취소
        String userDeviceToken = member.getUser().getDeviceToken();
        UserNotification memberUserNotification = notificationRepository
                .findByTypeAndUser(TopicEnum.BOARD, member.getUser()).orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_SERVER_ERROR));

        FCMTopic topic = FCMTopic.builder()
                .topic(TopicEnum.GROUP)
                .topicId(groupId)
                .build();
        if (userDeviceToken!=null && memberUserNotification.getStatus()){
            fcmService.unsubscribeFromTopic(userDeviceToken, topic);//구독 취소
            //해당 유저에 알림 발송
            Notification notification = Notification.builder()
                    .setTitle("[EGGLOG]")
                    .setBody(member.getGroup().getGroupName()+" 에서 퇴장 당했습니다.")
                    .build();
            fcmService.sendPersonalNotification(userDeviceToken, notification);
        }
    }

    @Transactional
    public void registerBoardNotification(BoardForm boardForm, Board saveBoard, Group group) {
        if (saveBoard.getBoardType().equals(BoardType.GROUP) && group != null) {
            //그룹 게시판에 글이 등록되었다면 푸시알림 발송
            FCMTopic topic = FCMTopic.builder()
                    .topic(TopicEnum.GROUP)
                    .topicId(boardForm.getGroupId())
                    .build();
            Notification notification = Notification.builder()
                    .setTitle("[EGGLOG] " + group.getGroupName() + " 커뮤니티에 새 글이 올라왔습니다.")
                    .setBody(boardForm.getBoardTitle())
                    .setImage(boardForm.getPictureOne() != null ? boardForm.getPictureOne() : null)
                    .build();
            fcmService.sendNotificationToTopic(topic, notification);
        }
    }

    @Transactional
    public void registerCommentNotification(Board board, Comment saveComment, Comment comment) {
        UserNotification userNotification = notificationRepository
                .findByTypeAndUser(TopicEnum.BOARD, board.getUser()).orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_SERVER_ERROR));
        //1. 해당 글에 댓글이 등록되었다면 글 작성자에게 푸시알림 발송
        String deviceToken = board.getUser().getDeviceToken();
        if (deviceToken!=null&&userNotification.getStatus()){
            Notification notification = Notification.builder()
                    .setTitle("[EGGLOG] 커뮤니티 글에 새 댓글이 달렸습니다.")
                    .setBody(saveComment.getContent())
                    .build();
            fcmService.sendPersonalNotification(deviceToken, notification);
        }


        // 2. 대 댓글이라면 부모 ID 작성자에게도 알림을 보낸다.
        Long parentId = comment.getParentId();
        if (!parentId.equals(0L)&&!parentId.equals(board.getUser().getId())){//대 댓글이면서 부모 댓글의 아이디가 글 작성자가 아닐때만 발송한다.
            Comment cocoment = commentRepository.findWithUserById(parentId).orElseThrow(
                    () -> new CommentException(CommentErrorCode.NO_EXIST_COMMENT));
            String cocomentDeviceToken = cocoment.getUser().getDeviceToken();
            UserNotification cocomentUserNotification = notificationRepository
                    .findByTypeAndUser(TopicEnum.BOARD, cocoment.getUser()).orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_SERVER_ERROR));
            if (cocomentDeviceToken!=null&&cocomentUserNotification.getStatus()){
                Notification notification = Notification.builder()
                        .setTitle("[EGGLOG] 내 댓글에 새 대댓글이 달렸습니다.")
                        .setBody(saveComment.getContent())
                        .build();
                fcmService.sendPersonalNotification(cocomentDeviceToken, notification);
            }
        }
    }
}
