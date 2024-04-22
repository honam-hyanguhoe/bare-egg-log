package org.egglog.api.group.model.service;

import lombok.RequiredArgsConstructor;
import org.egglog.api.group.exception.GroupMemberErrorCode;
import org.egglog.api.group.exception.GroupMemberException;
import org.egglog.api.group.model.entity.GroupMember;
import org.egglog.api.group.model.repository.GroupMemberJpaRepository;
import org.egglog.api.group.model.repository.GroupMemberQueryRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService{
    private final GroupMemberQueryRepository groupMemberQueryRepository;
    private final GroupMemberJpaRepository groupMemberJpaRepository;
    @Override
    public GroupMember getAdminMember(Long groupId) {
        GroupMember boss = groupMemberQueryRepository
                .findGroupBossMemberByGroupId(groupId)
                .orElseThrow(()->new GroupMemberException(GroupMemberErrorCode.NOT_FOUND));
        return boss;
    }

    @Override
    public List<GroupMember> getGroupMeberList(Long groupId) {
        List<GroupMember> groupMemberList = groupMemberQueryRepository
                .findGroupMemberByGroupId(groupId)
                .orElseThrow(() -> new GroupMemberException(GroupMemberErrorCode.NOT_FOUND));
        return groupMemberList;
    }

    @Override
    public GroupMember getGroupMember(Long groupId, Long userId) {
        GroupMember member = groupMemberQueryRepository
                .findGroupMemberByGroupIdAndUserId(groupId,userId)
                .orElseThrow(()->new GroupMemberException(GroupMemberErrorCode.NOT_FOUND));
        return member;
    }

    @Override
    public void createGroupMember(GroupMember groupMember) {
        try {
            groupMemberJpaRepository.save(groupMember);
        }catch (Exception e){
            throw new GroupMemberException(GroupMemberErrorCode.TRANSACTION_ERROR);
        }
    }


}
