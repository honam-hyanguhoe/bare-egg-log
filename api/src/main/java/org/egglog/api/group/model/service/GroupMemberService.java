package org.egglog.api.group.model.service;

import org.egglog.api.group.model.entity.GroupMember;

import java.util.List;

public interface GroupMemberService {
    GroupMember getAdminMember(Long groupId);
    List<GroupMember> getGroupMeberList(Long groupId);
    GroupMember getGroupMember(Long groupId, Long userId);
    void createGroupMember(GroupMember groupMember);
}
