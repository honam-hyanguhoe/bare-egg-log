package org.egglog.api.group.model.service;

import org.egglog.api.group.model.dto.request.GroupForm;
import org.egglog.api.group.model.dto.request.GroupUpdateForm;
import org.egglog.api.group.model.dto.request.InvitationAcceptForm;
import org.egglog.api.group.model.dto.response.GroupDto;
import org.egglog.api.group.model.dto.response.GroupMemberDto;

import java.util.List;


public interface GroupService {
    void deleteGroup(Long groupId);

    void acceptInvitation(InvitationAcceptForm acceptForm, Long userId);

    String getOrGenerateInvitation(Long groupId, Long userId);

    void deleteGroupMember(Long groupId, Long memberId, Long userId);

    List<GroupMemberDto> getGroupList(Long userId);

    GroupDto retrieveGroup(Long groupId, Long userId);

    void updateGroup(Long groupId, GroupUpdateForm groupUpdateForm, Long userId);

    void updateGroupMember(Long groupId, Long memberId, Long userId);

    void exitGroup(Long groupId, Long userId);

    void generateGroup(GroupForm groupForm, Long userId);
}
