package org.egglog.api.group.model.service;

import lombok.RequiredArgsConstructor;
import org.egglog.api.group.exception.GroupMemberErrorCode;
import org.egglog.api.group.exception.GroupMemberException;
import org.egglog.api.group.model.entity.GroupMember;
import org.egglog.api.group.repository.jpa.GroupMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;
    public GroupMember getAdminMember(Long groupId) {
        GroupMember boss = groupMemberRepository
                .findGroupBossMemberByGroupId(groupId)
                .orElseThrow(()->new GroupMemberException(GroupMemberErrorCode.NOT_FOUND));
        return boss;
    }

    public List<GroupMember> getGroupMeberList(Long groupId) {
        List<GroupMember> groupMemberList = groupMemberRepository
                .findGroupMemberByGroupId(groupId)
                .orElseThrow(() -> new GroupMemberException(GroupMemberErrorCode.NOT_FOUND));
        return groupMemberList;
    }

    public GroupMember getGroupMember(Long groupId, Long userId) {
        GroupMember member = groupMemberRepository
                .findGroupMemberByGroupIdAndUserId(groupId,userId)
                .orElseThrow(()->new GroupMemberException(GroupMemberErrorCode.NOT_FOUND));
        return member;
    }

    /**
     * 생성된 그룹원 DB 등록
     * @param groupMember
     */
    public void createGroupMember(GroupMember groupMember) {
        try {
            groupMemberRepository.save(groupMember);
        }catch (Exception e){
            throw new GroupMemberException(GroupMemberErrorCode.TRANSACTION_ERROR);
        }
    }
    /**
     * 그룹원 삭제
     * @param groupMember
     */
    public void deleteGroupMember(GroupMember groupMember) {
        try {
            groupMemberRepository.delete(groupMember);
        }catch (Exception e){
            throw new GroupMemberException(GroupMemberErrorCode.TRANSACTION_ERROR);
        }
    }

    /**
     * 그룹원 수 세기
     * @param groupId
     */
    public Integer countGroupMember(Long groupId){
        Integer count = groupMemberRepository.countGroupMember(groupId);
        if(count == null){
            throw new GroupMemberException(GroupMemberErrorCode.NOT_FOUND);
        }
        return count;
    }
}
