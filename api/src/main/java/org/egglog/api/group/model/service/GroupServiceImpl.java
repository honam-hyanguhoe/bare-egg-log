package org.egglog.api.group.model.service;

import lombok.RequiredArgsConstructor;
import org.egglog.api.group.model.dto.request.GroupForm;
import org.egglog.api.group.model.dto.request.GroupUpdateForm;
import org.egglog.api.group.model.dto.request.InvitationAcceptForm;
import org.egglog.api.group.model.dto.response.GroupDto;
import org.egglog.api.group.model.dto.response.GroupMemberDto;
import org.egglog.api.group.model.repository.GroupInvitationRepository;
import org.egglog.api.group.model.repository.GroupMemberQueryRepository;
import org.egglog.api.group.model.repository.GroupQueryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService{
    private final GroupQueryRepository groupQueryRepository;
    private final GroupMemberQueryRepository groupMemberQueryRepository;
    private final GroupInvitationRepository groupInvitationRepository;
    @Override
    public void deleteGroup(Long groupId) {

    }

    @Override
    public void acceptInvitation(InvitationAcceptForm acceptForm, Long userId) {

    }

    @Override
    public String getOrGenerateInvitation(Long groupId, Long userId) {
        return null;
    }

    @Override
    public void deleteGroupMember(Long groupId, Long memberId, Long userId) {

    }

    @Override
    public List<GroupMemberDto> getGroupList(Long userId) {
        return null;
    }

    @Override
    public GroupDto retrieveGroup(Long groupId, Long userId) {
        return null;
    }

    @Override
    public void updateGroup(Long groupId, GroupUpdateForm groupUpdateForm, Long userId) {

    }

    @Override
    public void updateGroupMember(Long groupId, Long memberId, Long userId) {

    }

    @Override
    public void exitGroup(Long groupId, Long userId) {

    }

    @Override
    public void generateGroup(GroupForm groupForm, Long userId) {

    }
}
