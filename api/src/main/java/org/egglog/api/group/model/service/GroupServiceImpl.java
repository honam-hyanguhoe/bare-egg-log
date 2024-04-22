package org.egglog.api.group.model.service;

import lombok.RequiredArgsConstructor;
import org.egglog.api.group.exception.GroupErrorCode;
import org.egglog.api.group.exception.GroupException;
import org.egglog.api.group.model.dto.request.GroupForm;
import org.egglog.api.group.model.dto.request.GroupUpdateForm;
import org.egglog.api.group.model.dto.request.InvitationAcceptForm;
import org.egglog.api.group.model.dto.response.GroupDto;
import org.egglog.api.group.model.dto.response.GroupMemberDto;
import org.egglog.api.group.model.entity.GroupMember;
import org.egglog.api.group.model.entity.InvitationCode;
import org.egglog.api.group.model.repository.GroupInvitationRepository;
import org.egglog.api.group.model.repository.GroupJpaRepository;
import org.egglog.api.group.model.repository.GroupQueryRepository;
import org.egglog.api.user.model.entity.Users;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService{
    private final PasswordEncoder passwordEncoder;
    private final GroupMemberService groupMemberService;
    private final GroupQueryRepository groupQueryRepository;
    private final GroupJpaRepository groupJpaRepository;
    private final GroupInvitationRepository groupInvitationRepository;

    /**
     * 그룹 삭제 메서드
     * @param groupId
     */
    @Override
    public void deleteGroup(Long groupId, Long userId) {
        GroupMember boss = groupMemberService.getGroupMember(groupId,userId);
        //만약 그룹장이 아니라면
        if(!boss.getIsAdmin()){
            // 권한이 없는 사용자의 요청입니다.
            throw new GroupException(GroupErrorCode.GROUP_ROLE_NOT_MATCH);
        }
        try {
            //group id로 그룹 삭제
            groupJpaRepository.deleteById(groupId);
        }catch (Exception e){
            //트랜잭션 실패
            throw new GroupException(GroupErrorCode.TRANSACTION_ERROR);
        }
    }

    @Override
    public void acceptInvitation(InvitationAcceptForm acceptForm, Users user) {
        //존재하지 않는 코드는 exception 처리
        InvitationCode invitationCode = groupInvitationRepository
                .findInvitationCodeById(acceptForm.getInvitationCode())
                .orElseThrow(() -> new GroupException(GroupErrorCode.NOT_FOUND_INVITATION));
        //password 검증
        //만약 password가 동일하다면 초대 수락
        if(passwordEncoder.encode(acceptForm.getPassword()).equals(invitationCode.getPassword())){
            GroupMember newMember = GroupMember.builder()
                    .groupId(invitationCode.getGroupId())
                    .user(user)
                    .isAdmin(false)
                    .build();
            groupMemberService.createGroupMember(newMember);
        }else{
            throw new GroupException(GroupErrorCode.NOT_MATCH_INVITATION);
        }
    }

    @Override
    public String getOrGenerateInvitation(Long groupId, Users user) {

        return null;
    }

    @Override
    public void deleteGroupMember(Long groupId, Long memberId, Users user) {

    }

    @Override
    public List<GroupMemberDto> getGroupList(Users user) {
        return null;
    }

    @Override
    public GroupDto retrieveGroup(Long groupId, Users user) {
        return null;
    }

    @Override
    public void updateGroup(Long groupId, GroupUpdateForm groupUpdateForm, Long userId) {

    }

    @Override
    public void updateGroupMember(Long groupId, Long memberId, Users user) {

    }

    @Override
    public void exitGroup(Long groupId, Users user) {

    }

    @Override
    public void generateGroup(GroupForm groupForm, Long userId) {

    }
}
