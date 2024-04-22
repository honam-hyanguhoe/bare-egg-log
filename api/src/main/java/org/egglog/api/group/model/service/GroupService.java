package org.egglog.api.group.model.service;

import org.egglog.api.group.model.dto.request.GroupForm;
import org.egglog.api.group.model.dto.request.GroupUpdateForm;
import org.egglog.api.group.model.dto.request.InvitationAcceptForm;
import org.egglog.api.group.model.dto.response.GroupDto;
import org.egglog.api.group.model.dto.response.GroupMemberDto;
import org.egglog.api.user.model.entity.Users;

import java.util.List;


public interface GroupService {
    void deleteGroup(Long groupId, Long userId);

    void acceptInvitation(InvitationAcceptForm acceptForm, Users user);

    String getOrGenerateInvitation(Long groupId, Users user);

    void deleteGroupMember(Long groupId, Long memberId, Users user);

    List<GroupMemberDto> getGroupList(Users user);

    GroupDto retrieveGroup(Long groupId, Users user);

    void updateGroup(Long groupId, GroupUpdateForm groupUpdateForm, Long userId);

    void updateGroupMember(Long groupId, Long memberId, Users user);

    void exitGroup(Long groupId, Users user);

    void generateGroup(GroupForm groupForm, Long userId);
}
