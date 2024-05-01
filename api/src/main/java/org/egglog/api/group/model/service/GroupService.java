package org.egglog.api.group.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egglog.api.group.exception.GroupErrorCode;
import org.egglog.api.group.exception.GroupException;
import org.egglog.api.group.model.dto.request.GroupForm;
import org.egglog.api.group.model.dto.request.GroupUpdateForm;
import org.egglog.api.group.model.dto.request.InvitationAcceptForm;
import org.egglog.api.group.model.dto.response.*;
import org.egglog.api.group.model.entity.Group;
import org.egglog.api.group.model.entity.GroupMember;
import org.egglog.api.group.model.entity.InvitationCode;
import org.egglog.api.group.repository.redis.GroupInvitationRepository;
import org.egglog.api.group.repository.jpa.GroupRepository;
import org.egglog.api.user.model.entity.User;
import org.egglog.utility.utils.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupService {
    private final PasswordEncoder passwordEncoder;
    private final GroupMemberService groupMemberService;

    private final GroupRepository groupRepository;
    private final GroupInvitationRepository groupInvitationRepository;


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
    public void deleteGroupMember(Long groupId, Long memberId, User user) {
        GroupMember boss = groupMemberService.getAdminMember(groupId);
        if(boss.getUser().getId()==user.getId()) {
            //그룹에 해당 멤버가 존재하는지 검증하고 삭제
            GroupMember member = groupMemberService.getGroupMember(groupId, memberId);
            groupMemberService.deleteGroupMember(member);
        }
    }

    /**
     * 본인이 속한 그룹리스트 반환
     * @param user
     * @return
     */
    public List<GroupPreviewDto> getGroupList(User user) {
        List<GroupPreviewDto> groupList = groupRepository.findGroupByUserId(user.getId()).orElse(null);
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
        GroupMember boss = groupMemberService.getAdminMember(groupId);
        Boolean isBoss = false;
        if(boss.getUser().getId() == user.getId()){
            isBoss = true;
        }
        List<GroupMemberDto> memberList = groupMemberService
                .getGroupMeberList(groupId)
                .stream()
                .map(GroupMember::toDto)
                .collect(Collectors.toList());

        return GroupDto.builder()
                .id(groupId)
                .groupImage(group.getGroupImage())
                .groupName(group.getGroupName())
                .admin(boss.toDto())
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
            if(groupUpdateForm.getNewName()!=null){
                group.setGroupName(groupUpdateForm.getNewName());
            }
            if(groupUpdateForm.getNewPassword()!=null){
                group.setPassword(passwordEncoder.encode(groupUpdateForm.getNewPassword()));
                InvitationCode invitationCode = groupInvitationRepository.findInvitationCodeByGroupId(groupId).orElse(null);
                if(invitationCode!=null){
                    invitationCode.setPassword(group.getPassword());
                    groupInvitationRepository.save(invitationCode);
                }
            }
            groupRepository.save(group);
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
    public BossChangeDto updateGroupMember(Long groupId, Long memberId, User user) {
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

        //응답 DTO 생성
        BossChangeDto changes = new BossChangeDto();
        changes.setCurrentAdmin(newBoss.toDto());
        changes.setOldAdmin(boss.toDto());

        return changes;
    }

    /**
     * 그룹 탈퇴
     * 1. 그룹장은 혼자 남았을 경우에만 탈퇴 가능
     * 2. 그룹원은 언제나 탈퇴 가능
     * @param groupId
     * @param user
     */
    public void exitGroup(Long groupId, User user) {
        GroupMember userInfo = groupMemberService.getGroupMember(groupId, user.getId());
        //그룹장인지 확인
        if(userInfo.getIsAdmin()){
            Integer count = groupMemberService.countGroupMember(groupId);
            //혼자가 아니라면 탈퇴 권한 없음
            if(count==1){
                groupMemberService.deleteGroupMember(userInfo);
                //더이상 남은 사용자가 없으니 그룹 삭제
                Group group = groupRepository.findById(groupId).orElseThrow(()->new GroupException(GroupErrorCode.NOT_FOUND));
                groupRepository.delete(group);
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
    public void generateGroup(GroupForm groupForm, User user) {
        Group newGroup = Group.builder()
                .admin(user.getName())
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
            groupRepository.save(newGroup);
            groupMemberService.createGroupMember(newGroupMember);
        }catch (Exception e){
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        }
    }

    public GroupDutySummary getGroupDuty(Long groupId, User user, String date) {
        if(!groupMemberService.isGroupMember(groupId,user.getId())){
            throw new GroupException(GroupErrorCode.GROUP_ROLE_NOT_MATCH);
        }
        return groupMemberService.getGroupDutySummary(groupId, date);
    }
}
