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
import org.egglog.api.group.model.dto.request.GroupDutyData;
import org.egglog.api.group.model.dto.response.GroupPreviewDto;
import org.egglog.api.group.model.entity.Group;
import org.egglog.api.group.model.entity.GroupMember;
import org.egglog.api.group.repository.jpa.GroupRepository;
import org.egglog.api.hospital.model.entity.HospitalAuth;
import org.egglog.api.notification.exception.NotificationErrorCode;
import org.egglog.api.notification.exception.NotificationException;
import org.egglog.api.notification.model.dto.request.NotificationRequest;
import org.egglog.api.notification.model.dto.response.NotificationResponse;
import org.egglog.api.notification.model.entity.FCMTopic;
import org.egglog.api.notification.model.entity.UserNotification;
import org.egglog.api.notification.model.entity.enums.DeepLinkConfig;
import org.egglog.api.notification.model.entity.enums.TopicEnum;
import org.egglog.api.notification.repository.jpa.NotificationRepository;
import org.egglog.api.user.model.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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
    private final DeepLinkConfig deepLinkConfig;

    //설정 생성
    @Transactional
    public void makeDefaultNotification(User loginUser){
        log.debug(" ==== ==== ==== [ 기본 알람 설정 생성 서비스 실행 ] ==== ==== ====");
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
        log.debug(" ==== ==== ==== [ 알림 설정 변경 서비스 실행 ] ==== ==== ====");
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
        log.debug(" ==== ==== ==== [ 알림 설정 리스트 조회 서비스 실행 ] ==== ==== ====");
        return notificationRepository.findListByUser(loginUser)
                .stream()
                .map(UserNotification::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateTopicSubscribeState(String oldToken, User loginUser){
        log.debug(" ==== ==== ==== [ 토픽 재구독 서비스 실행 ] ==== ==== ====");
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
                if (oldToken!=null) fcmService.unsubscribeFromTopic(oldToken, topic); //예전 토큰 구독 취소
                fcmService.subscribeToTopic(newToken, topic); //새 토큰으로 구독
            }
        }

    }

    @Transactional
    public void groupMemberAcceptNotification(User user, Group group) {
        log.debug(" ==== ==== ==== [ 그룹 멤버 가입 알림 및 구독 서비스 실행 ] ==== ==== ====");
        //1. 그룹에 새 멤버가 추가되었다면 해당 그룹 토픽으로 FCM 알림 발송
        FCMTopic topic = FCMTopic.builder()
                .topic(TopicEnum.GROUP)
                .topicId(group.getId())
                .build();
        fcmService.sendNotificationToTopic(topic, "피어나! 너 내 동료가 내 ㄷ도 동료가 돼라!",group.getGroupName()+"의 그룹에 새 멤버가 추가 되었습니다. 축하해주세요!", makeDeepLinkSetting(deepLinkConfig.getGroup(), group.getId()));

        //2. 그룹에 가입되었다면 해당 그룹 토픽 구독
        String loginUserDeviceToken = user.getDeviceToken();
        if (loginUserDeviceToken!=null){
            fcmService.subscribeToTopic(loginUserDeviceToken, topic);
        }
    }

    @Transactional
    public void createGroupNotification(User loginUser, Group group){
        log.debug(" ==== ==== ==== [ 그룹 멤버 가입 구독 서비스 실행 ] ==== ==== ====");
        FCMTopic topic = FCMTopic.builder()
                .topic(TopicEnum.GROUP)
                .topicId(group.getId())
                .build();
        String loginUserDeviceToken = loginUser.getDeviceToken();
        if (loginUserDeviceToken!=null){
            fcmService.subscribeToTopic(loginUserDeviceToken, topic);
        }
    }
    @Transactional
    public void exitGroupNotification(User user, Long groupId){
        log.debug(" ==== ==== ==== [ 그룹 멤버 탈퇴 구독 취소 서비스 실행 ] ==== ==== ====");
        String userDeviceToken = user.getDeviceToken();
        FCMTopic topic = FCMTopic.builder()
                .topic(TopicEnum.GROUP)
                .topicId(groupId)
                .build();
        if (userDeviceToken!=null) {
            fcmService.unsubscribeFromTopic(userDeviceToken, topic);//구독 취소
        }
    }

    @Transactional
    public void deleteGroupMemberNotification(Long groupId, GroupMember member) {
        log.debug(" ==== ==== ==== [ 그룹 멤버 강퇴 구독 취소 및 알림 서비스 실행 ] ==== ==== ====");
        //해당 멤버가 삭제되었다면 해당 유저의 토픽 구독 취소
        String userDeviceToken = member.getUser().getDeviceToken();
        //알림을 거부했는지 확인
        UserNotification memberUserNotification = notificationRepository
                .findByTypeAndUser(TopicEnum.BOARD, member.getUser()).orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_SERVER_ERROR));

        FCMTopic topic = FCMTopic.builder()
                .topic(TopicEnum.GROUP)
                .topicId(groupId)
                .build();
        fcmService.unsubscribeFromTopic(userDeviceToken, topic);//구독 취소
        if (userDeviceToken!=null && memberUserNotification.getStatus()){
            //해당 유저에 알림 발송
            fcmService.sendPersonalNotification(userDeviceToken, "시원 섭섭하네.. ",member.getGroup().getGroupName()+" 에서 퇴장 당했습니다.", makeDeepLinkSetting(deepLinkConfig.getGroup()));
        }
    }

    @Transactional
    public void registerBoardNotification(BoardForm boardForm, Board saveBoard, Group group) {
        log.debug(" ==== ==== ==== [ 그룹 커뮤니티 새 글 알림 서비스 실행 ] ==== ==== ====");
        if (saveBoard.getBoardType().equals(BoardType.GROUP) && group != null) {
            //그룹 게시판에 글이 등록되었다면 푸시알림 발송
            FCMTopic topic = FCMTopic.builder()
                    .topic(TopicEnum.GROUP)
                    .topicId(boardForm.getGroupId())
                    .build();
            fcmService.sendNotificationToTopic(topic, "[새 글 알림]" + group.getGroupName() + " 커뮤니티에 새 글이 올라왔습니다.", boardForm.getBoardTitle(), makeDeepLinkSetting(deepLinkConfig.getCommunity(), saveBoard.getId()));
        }
    }

    @Transactional
    public void hotBoardNotification(Board board){
        log.debug(" ==== ==== ==== [ 실시간 게시판 등록 알림 서비스 실행 ] ==== ==== ====");
        //1. 해당 글이 실시간 급상승 게시판에 등록되면 글 작성자에게 푸시알림 발송
        UserNotification userNotification = notificationRepository
                .findByTypeAndUser(TopicEnum.BOARD, board.getUser()).orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_SERVER_ERROR));
        String deviceToken = board.getUser().getDeviceToken();
        if (deviceToken!=null&&userNotification.getStatus()){
            fcmService.sendPersonalNotification(deviceToken, "대세는 바로 나! 인기 게시물 선정!",board.getUser().getName()+" 님의 "+board.getTitle()+" 글이 실시간 급상승 게시판에 등록되었습니다. ", makeDeepLinkSetting(deepLinkConfig.getCommunity(), board.getId()));
        }
    }


    @Transactional
    public void registerCommentNotification(Board board, Comment saveComment) {
        log.debug(" ==== ==== ==== [ 댓글 등록 알림 서비스 실행 ] ==== ==== ====");
        UserNotification userNotification = notificationRepository
                .findByTypeAndUser(TopicEnum.BOARD, board.getUser()).orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_SERVER_ERROR));
        //1. 해당 글에 댓글이 등록되었다면 글 작성자에게 푸시알림 발송
        String deviceToken = board.getUser().getDeviceToken();
        if (deviceToken!=null&&userNotification.getStatus()&&!board.getUser().equals(saveComment.getUser())){
            fcmService.sendPersonalNotification(deviceToken, "띠용! 내 커뮤니티 글에 새 댓글이 달렸습니다.", saveComment.getContent(), makeDeepLinkSetting(deepLinkConfig.getCommunity(), board.getId()));
        }


        // 2. 대 댓글이라면 부모 ID 작성자에게도 알림을 보낸다.
        Long parentId = saveComment.getParentId();
        User commentUser = saveComment.getUser();
        if (!parentId.equals(0L)){//대 댓글일때만 발송 준비
            Comment parentComment = commentRepository.findWithUserById(parentId).orElseThrow(
                    () -> new CommentException(CommentErrorCode.NO_EXIST_COMMENT));
            String parentComentDeviceToken = parentComment.getUser().getDeviceToken();
            UserNotification parentComentUserNotification = notificationRepository
                    .findByTypeAndUser(TopicEnum.BOARD, parentComment.getUser()).orElseThrow(() -> new NotificationException(NotificationErrorCode.NOTIFICATION_SERVER_ERROR));
            if (parentComentDeviceToken!=null&&parentComentUserNotification.getStatus()&&!commentUser.equals(parentComment.getUser())&&!commentUser.equals(board.getUser())){
                //부모 댓글 FCM 토큰이 Null 이 아니고, 부모 댓글 알림 설정이 켜져있고, 대댓글 유저가 부모 댓글 유저와 같이 않고, 대댓글 유저가 글 작성 유저와 같지 않으면 FCM 알림을 발송한다.
                fcmService.sendPersonalNotification(parentComentDeviceToken, "띠용! 내 댓글에 새 대댓글이 달렸습니다.",saveComment.getContent(), makeDeepLinkSetting(deepLinkConfig.getCommunity(), board.getId()));
            }
        }
    }
    @Transactional
    public void iamBossNotification(Group group, User newBoss){
        log.debug(" ==== ==== ==== [ 재직 인증 승인 알림 서비스 실행 ] ==== ==== ====");
        String deviceToken = newBoss.getDeviceToken();
        UserNotification userNotification = notificationRepository.findByTypeAndUser(TopicEnum.GROUP, newBoss).orElseThrow(
                () -> new CommentException(CommentErrorCode.NO_EXIST_COMMENT));
        if (deviceToken!=null&&userNotification.getStatus()){
            fcmService.sendPersonalNotification(deviceToken, "이제 고생 시작이다..",group.getGroupName()+"의 그룹장이 되셨습니다.", makeDeepLinkSetting(deepLinkConfig.getGroup(), group.getId()));
        }
    }

    @Transactional
    public void newBossNotification(Group group, Board board, User newBoss){
        log.debug(" ==== ==== ==== [ 새로운 그룹장 알림 서비스 실행 ] ==== ==== ====");
        String deviceToken = newBoss.getDeviceToken();
        UserNotification userNotification = notificationRepository.findByTypeAndUser(TopicEnum.GROUP, newBoss).orElseThrow(
                () -> new CommentException(CommentErrorCode.NO_EXIST_COMMENT));
        FCMTopic topic = FCMTopic.builder()
                .topic(TopicEnum.GROUP)
                .topicId(group.getId())
                .build();
        if (deviceToken!=null&&userNotification.getStatus()){
            fcmService.sendNotificationToTopic(topic, "NEW BOSS 등장! ",group.getGroupName()+"의 새로운 BOSS "+newBoss.getName()+" 님에게 축하 댓글을 달아주세요!", makeDeepLinkSetting(deepLinkConfig.getCommunity(), board.getId()));
        }
    }

    @Transactional
    public void certHospitalNotification(HospitalAuth hospitalAuth){
        log.debug(" ==== ==== ==== [ 재직 인증 승인 알림 서비스 실행 ] ==== ==== ====");
        User user = hospitalAuth.getUser();
        String deviceToken = user.getDeviceToken();
        UserNotification userNotification = notificationRepository.findByTypeAndUser(TopicEnum.SYSTEM, user).orElseThrow(
                () -> new CommentException(CommentErrorCode.NO_EXIST_COMMENT));
        if (deviceToken!=null&&userNotification.getStatus()){
            fcmService.sendPersonalNotification(deviceToken, "나도 진짜 간호사라구",hospitalAuth.getHospital() + "의 재직 및 간호 인증이 완료 되었습니다.", makeDeepLinkSetting(deepLinkConfig.getMain()));
            }
    }

    @Transactional
    public void excelDutyUploadNotification(Long groupId, GroupDutyData groupData){
        int month = YearMonth.parse(groupData.getDate(), DateTimeFormatter.ofPattern("yyyy-MM")).getMonthValue();
        String groupName = groupData.getGroupName();
        //그룹에 엑셀 동기화 파일이 등록되었다면 푸시알림 발송
        FCMTopic topic = FCMTopic.builder()
                .topic(TopicEnum.GROUP)
                .topicId(groupId)
                .build();
        fcmService.sendNotificationToTopic(topic, "개이득주의! 새 근무 파일이 날라왔어요",groupName+" 그룹에 "+month+"월 근무표가 업로드 되었습니다.", makeDeepLinkSetting(deepLinkConfig.getCalendar()));
    }

    private Map<String, String> makeDeepLinkSetting(String deeplink){
        Map<String, String> map = new HashMap<>();
        map.put("click_action", deeplink);
        return map;
    }
    private Map<String, String> makeDeepLinkSetting(String deeplink, Long id){
        Map<String, String> map = new HashMap<>();
        map.put("click_action", deeplink + "/" + id);
        return map;
    }
}
