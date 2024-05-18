package org.egglog.api.group.model.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.board.model.entity.Board;
import org.egglog.api.board.repository.jpa.board.BoardRepository;
import org.egglog.api.group.exception.GroupErrorCode;
import org.egglog.api.group.exception.GroupException;
import org.egglog.api.group.model.dto.request.*;
import org.egglog.api.group.model.dto.response.*;
import org.egglog.api.group.model.entity.Group;
import org.egglog.api.group.model.entity.GroupMember;
import org.egglog.api.group.model.entity.InvitationCode;
import org.egglog.api.group.repository.firestore.GroupDutyRepository;
import org.egglog.api.group.repository.redis.GroupInvitationRepository;
import org.egglog.api.group.repository.jpa.GroupRepository;
import org.egglog.api.notification.model.entity.FCMTopic;
import org.egglog.api.notification.model.entity.enums.TopicEnum;
import org.egglog.api.notification.model.service.FCMService;
import org.egglog.api.notification.model.service.NotificationService;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.worktype.model.entity.WorkTag;
import org.egglog.api.worktype.model.entity.WorkType;
import org.egglog.utility.utils.RandomStringUtils;
import org.egglog.utility.utils.SuccessType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {
    private final PasswordEncoder passwordEncoder;
    private final GroupMemberService groupMemberService;

    // 그룹 관련 레포지토리
    private final GroupRepository groupRepository;
    private final GroupDutyRepository groupDutyRepository;
    private final GroupInvitationRepository groupInvitationRepository;

    // 게시판 관련 레포지토리(그룹 삭제를 위함)
    private final BoardRepository boardRepository;


    private final FCMService fcmService;
    private final NotificationService notificationService;
    /**
     * 초대수락 메서드
     * 존재하는 코드인지 확인해서 password 검증 후 그룹에 사용자 등록
     * @param acceptForm
     * @param user
     */
    public void acceptInvitation(InvitationAcceptForm acceptForm, User user) {
        log.debug(acceptForm.toString());
        //존재하지 않는 코드는 exception 처리
        InvitationCode invitationCode = groupInvitationRepository
                .findInvitationCodeByCode(acceptForm.getInvitationCode())
                .orElseThrow(() -> new GroupException(GroupErrorCode.NOT_FOUND_INVITATION));
        log.debug("\nin : {}\norigin : {}\nmatch : {}",
                acceptForm.getPassword(),
                invitationCode.getPassword(),
                passwordEncoder.matches(acceptForm.getPassword(),invitationCode.getPassword())
                );
        //password 검증
        //만약 password가 동일하다면 초대 수락
        if(passwordEncoder.matches(acceptForm.getPassword(), invitationCode.getPassword())){
            Group group = groupRepository
                    .findById(invitationCode.getGroupId())
                    .orElseThrow(() -> new GroupException(GroupErrorCode.NOT_FOUND));
            //이미 그룹원이라면 추가하지 않는다.
            if(groupMemberService.isGroupMember(group.getId(), user.getId())){
                throw new GroupException(GroupErrorCode.DUPLICATED_MEMBER);
            }
            GroupMember newMember = GroupMember.builder()
                    .group(group)
                    .user(user)
                    .isAdmin(false)
                    .build();
            groupMemberService.createGroupMember(newMember);
            notificationService.groupMemberAcceptNotification(user, group);
        }else{
            throw new GroupException(GroupErrorCode.NOT_MATCH_INVITATION);
        }
    }



    /**
     * 초대코드를 요청 받을때
     * 1. 그룹의 초대 코드가 존재한다면 해당 코드를 반환
     * 2. 그룹의 초대 코드가 존재하지않는다면 코드를 생성하여 반환
     * @param groupId
     * @param user
     * @return
     */
    public String getOrGenerateInvitation(Long groupId, User user) {
        //기존 초대코드 존재 여부 확인
        InvitationCode invitationCode = groupInvitationRepository
                .findInvitationCodeByGroupId(groupId)
                .orElse(null);
        //초대코드 생성 로직
        if(invitationCode==null){
            Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupException(GroupErrorCode.NOT_FOUND));
            invitationCode = InvitationCode.builder()
                    .groupId(groupId)
                    .code(RandomStringUtils.generateRandomMixChar(10))
                    .password(group.getPassword())
                    .build();
        }
        try {
            groupInvitationRepository.save(invitationCode);
        }catch (Exception e){
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        }
        return invitationCode.getCode();
    }

    /**
     * 그룹원 삭제
     * @param groupId
     * @param memberId
     * @param user
     */
    @Transactional
    public void deleteGroupMember(Long groupId, Long memberId, User user) {
        GroupMemberDto boss = groupMemberService.getAdminMember(groupId);
        if(Objects.equals(boss.getUserId(), user.getId())) {
            //그룹에 해당 멤버가 존재하는지 검증하고 삭제
            GroupMember member = groupMemberService.getGroupMember(groupId, memberId);
            groupMemberService.deleteGroupMember(member);

            notificationService.deleteGroupMemberNotification(groupId, member);
        }
    }



    /**
     * 본인이 속한 그룹리스트 반환
     * @param user
     * @return
     */
    public List<GroupPreviewDto> getGroupList(User user) {
        List<GroupPreviewDto> groupList = groupRepository.findGroupByUserId(user.getId());
        return groupList;
    }

    /**
     * 그룹 조회
     * @param groupId
     * @param user
     * @return
     */
    public GroupDto retrieveGroup(Long groupId, User user) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new GroupException(GroupErrorCode.NOT_FOUND));
        log.debug("getAdmin");
        GroupMemberDto boss = groupMemberService.getAdminMember(groupId);
        Boolean isBoss = false;

        if(Objects.equals(boss.getUserId(), user.getId())){
            isBoss = true;
        }

        log.debug("getMembers");
        List<GroupMemberDto> memberList = groupMemberService.getGroupMemberList(groupId);

        return GroupDto.builder()
                .id(groupId)
                .groupImage(group.getGroupImage())
                .groupName(group.getGroupName())
                .admin(boss)
                .isAdmin(isBoss)
                .groupMembers(memberList)
                .build();
    }

    /**
     * 그룹 수정
     * 1. 그룹장은 그룹을 수정할 수 있다.
     * @param groupId
     * @param groupUpdateForm
     * @param userId
     */
    @Transactional
    public GroupSimpleDto updateGroup(Long groupId, GroupUpdateForm groupUpdateForm, Long userId) {
        GroupMember user = groupMemberService.getGroupMember(groupId,userId);
        //그룹장이 아니라면 수정 권한이 없음
        if(!user.getIsAdmin()){
            throw new GroupException(GroupErrorCode.GROUP_ROLE_NOT_MATCH);
        }
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupException(GroupErrorCode.NOT_FOUND));
        try {
            Group updateGroup = group.update(groupUpdateForm.getNewName(), passwordEncoder.encode(groupUpdateForm.getNewPassword()));
            InvitationCode invitationCode = groupInvitationRepository.findInvitationCodeByGroupId(groupId)
                    .orElse(InvitationCode
                            .builder()
                            .groupId(groupId)
                            .code(RandomStringUtils.generateRandomMixChar(10))
                            .build());

            invitationCode.setPassword(group.getPassword());
            groupRepository.save(updateGroup);
            groupInvitationRepository.save(invitationCode);

        }catch (Exception e){
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        }
        return group.toSimpleDto();
    }

    /**
     * 그룹장 권한 위임
     * @param groupId
     * @param memberId
     * @param user
     */
    @Transactional
    public void updateGroupMember(Long groupId, Long memberId, User user) {
        GroupMember boss = groupMemberService.getGroupMember(groupId, user.getId());
        //사용자가 그룹장이 아니라면 권한 에러 403
        if(!boss.getIsAdmin()){
            throw new GroupException(GroupErrorCode.GROUP_ROLE_NOT_MATCH);
        }
        //새로운 그룹장 생성
        GroupMember newBoss = groupMemberService.getGroupMember(groupId,memberId);

        newBoss.setIsAdmin(true);
        boss.setIsAdmin(false);

        if(newBoss == boss){
            throw new GroupException(GroupErrorCode.GROUP_ROLE_NOT_MATCH);
        }
        //변경 정보 DB 반영
        groupMemberService.createGroupMember(newBoss);
        groupMemberService.createGroupMember(boss);
    }

    /**
     * 그룹 탈퇴
     * 1. 그룹장은 혼자 남았을 경우에만 탈퇴 가능
     * 2. 그룹원은 언제나 탈퇴 가능
     * @param groupId
     * @param user
     */
    @Transactional
    public void exitGroup(Long groupId, User user) {
        GroupMember userInfo = groupMemberService.getGroupMember(groupId, user.getId());
        //그룹장인지 확인
        if(userInfo.getIsAdmin()){
            Integer count = groupMemberService.countGroupMember(groupId);
            //혼자가 아니라면 탈퇴 권한 없음
            if(count==1){
                groupMemberService.deleteGroupMember(userInfo);
                boardRepository.deleteByGroupId(groupId);
                //더이상 남은 사용자가 없으니 그룹 삭제
                Group group = groupRepository.findById(groupId).orElseThrow(()->new GroupException(GroupErrorCode.NOT_FOUND));
                groupRepository.delete(group);

                //해당 멤버가 삭제되었다면 해당 유저의 토픽 구독 취소
                notificationService.exitGroupNotification(user, groupId);


            }else{
                throw new GroupException(GroupErrorCode.GROUP_ROLE_NOT_MATCH);
            }
        }
        groupMemberService.deleteGroupMember(userInfo);
    }

    /**
     * 그룹 생성
     * 1. 생성자는 자동으로 그룹장으로 설정된다.
     * @param groupForm
     * @param user
     */
    @Transactional
    public void generateGroup(GroupForm groupForm, User user) {
        Group newGroup = Group.builder()
                .groupImage(groupForm.getGroupImage())
                .groupName(groupForm.getGroupName())
                .password(passwordEncoder.encode(groupForm.getGroupPassword()))
                .build();

        GroupMember newGroupMember = GroupMember
                .builder()
                .isAdmin(true)
                .group(newGroup)
                .user(user)
                .build();

        try {
            Group createGroup = groupRepository.save(newGroup);
            groupMemberService.createGroupMember(newGroupMember);
            //토픽 구독이 필요함
            notificationService.createGroupNotification(user, createGroup);
        }catch (Exception e){
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        }
    }

    /**
     * 그룹내 듀티별 해당자 요약
     * @param groupId
     * @param user
     * @param date
     * @return
     */
    public GroupDutySummary getGroupDuty(Long groupId, User user, String date) {
        if(!groupMemberService.isGroupMember(groupId,user.getId())){
            throw new GroupException(GroupErrorCode.GROUP_ROLE_NOT_MATCH);
        }
        return groupMemberService.getGroupDutySummary(groupId, date);
    }

    /**
     * 파싱된 엑셀 데이터 firestore에 저장
     * @param user
     * @param groupId
     * @param groupDutyData
     */
    public void addDuty(User user, Long groupId, GroupDutyData groupDutyData) {
        if(groupMemberService.isGroupMember(groupId,user.getId())){
            try {
                //출력을 위한 날짜 데이터
                SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
                Date now = new Date();
                String nowDay = dayFormat.format(now);
                log.debug("save Duty >>>>>");
                groupDutyRepository.saveDuty(user.getName(),groupId,groupDutyData,nowDay);
                log.debug("sending notification >>>>>");
                notificationService.excelDutyUploadNotification(groupId, groupDutyData);
            }catch (Exception e){
                throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
            }
        }else {
            throw new GroupException(GroupErrorCode.GROUP_ROLE_NOT_MATCH);
        }
    }

    /**
     * 등록된 엑셀 데이터 반환
     * @param user
     * @return
     */
    public List<GroupDutyDto> getGroupDutyList(User user, String date) {
        List<Long> groupList = groupMemberService.getUserGroupIdList(user.getId());
        try{
            return groupDutyRepository.getDutyListByGroupIdList(groupList,date);
        }catch (Exception e){
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        }
    }

    /**
     * 저장된 그룹 근무 태그 반환
     * @param user
     * @param groupId
     * @return
     */
    public CustomWorkTag getGroupWorkTags(User user, Long groupId) {
        if(groupMemberService.isGroupMember(groupId, user.getId())){
            try {
                CustomWorkTag customWorkTag = groupDutyRepository.getGroupWorkTag(groupId);
                return customWorkTag==null? new CustomWorkTag() : customWorkTag;
            } catch (Exception e){
                throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
            }
        } else {
            throw new GroupException(GroupErrorCode.GROUP_ROLE_NOT_MATCH);
        }
    }

    public Map<LocalDate, WorkType> getUserExcelData(Long groupId, LocalDate targetMonth, Long index, User loginUser) {
        log.debug("=== === === === 파이어 베이스에서 근무 데이터 가져오기 시작 === === === ===");
        Firestore db = FirestoreClient.getFirestore();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String date = targetMonth.format(formatter);
        String empNo = loginUser.getEmpNo();
        log.debug("=== === === === 경로 설정 시작 === === === ===");
        // Firestore 경로 설정
        CollectionReference dateCollection = db.collection("duty")
                .document(String.valueOf(groupId))
                .collection(date);

        ApiFuture<QuerySnapshot> future = dateCollection.get();
        Map<LocalDate, WorkType> workTypeMap = new HashMap<>();
        log.debug("=== === === === 데이터 가져오기 성공 === === === ===");
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            log.debug("=== List<QueryDocumentSnapshot> documents = {} " , documents);
            for (QueryDocumentSnapshot document : documents) {
                log.debug("=== QueryDocumentSnapshot document = {} " , document);
                if (document.exists() && document.getId().equals(String.valueOf(index))) {
                    // dutyList 맵에서 empNo를 키로 사용하여 WorkType 데이터를 가져옴
                    Map<String, Object> dutyList = (Map<String, Object>) document.get("dutyList");
                    log.debug("=== (Map<String, Object>) document.get(\"dutyList\") = {} " , dutyList);
                    if (dutyList != null && dutyList.containsKey(empNo)) {
                        Map<String, Object> workTypeData = (Map<String, Object>) dutyList.get(empNo);
                        log.debug("=== (Map<String, Object>) dutyList.get(empNo) = {} " , workTypeData);
                        for (Map.Entry<String, Object> entry : workTypeData.entrySet()) {
                            String day = entry.getKey();
                            String workTypeStr = (String) entry.getValue();
                            log.debug("=== entry.getKey() = {} " , day);
                            log.debug("=== (String) entry.getValue() = {} " , workTypeStr);
                            // workTypeStr을 WorkType 객체로 변환
                            WorkType workType = WorkType.builder().workTag(WorkTag.valueOf(workTypeStr)).build();
                            log.debug("=== workTypeStr을 WorkType 객체로 변환 workType = {} " , workType);
                            // LocalDate workDate 생성
                            LocalDate workDate = LocalDate.of(targetMonth.getYear(), targetMonth.getMonthValue(), Integer.parseInt(day));
                            log.debug("=== LocalDate workDate 생성 workDate = {} " , workDate);
                            // workTypeMap에 추가
                            workTypeMap.put(workDate, workType);
                        }
                    }
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        }

        return workTypeMap;
    }

}
