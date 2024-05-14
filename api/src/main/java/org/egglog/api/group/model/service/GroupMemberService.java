package org.egglog.api.group.model.service;

import lombok.RequiredArgsConstructor;
import org.egglog.api.group.exception.GroupMemberErrorCode;
import org.egglog.api.group.exception.GroupMemberException;
import org.egglog.api.group.model.dto.response.GroupDutySummary;
import org.egglog.api.group.model.dto.response.GroupMemberDto;
import org.egglog.api.group.model.entity.GroupMember;
import org.egglog.api.group.repository.jpa.GroupMemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;
    public GroupMemberDto getAdminMember(Long groupId) {
        return groupMemberRepository
                .findGroupBossMemberByGroupId(groupId);
    }

    public List<GroupMemberDto> getGroupMemberList(Long groupId) {
        return groupMemberRepository.findGroupMemberByGroupId(groupId);
    }

    public GroupMember getGroupMember(Long groupId, Long userId) {
        GroupMember member = groupMemberRepository
                .findGroupMemberByGroupIdAndUserId(groupId, userId)
                .orElseThrow(()->new GroupMemberException(GroupMemberErrorCode.NOT_FOUND));
        return member;
    }

    /**
     * 그룹원 검색에 성공하면 true, 실패하면 false 반환
     * @param groupId
     * @param userId
     * @return
     */
    public Boolean isGroupMember(Long groupId, Long userId){
        GroupMember member = groupMemberRepository
                .findGroupMemberByGroupIdAndUserId(groupId,userId)
                .orElse(null);
        return (member == null) ? false : true;
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

    /**
     *
     */
    public GroupDutySummary getGroupDutySummary(Long groupId,String date){
        String[] parsedDate=date.split("-");
        LocalDate targetDate = LocalDate.of(Integer.parseInt(parsedDate[0]),Integer.parseInt(parsedDate[1]),Integer.parseInt(parsedDate[2]));
        GroupDutySummary groupDutySummary = groupMemberRepository.getGroupDutySummary(groupId,targetDate);
        return groupDutySummary;
    }
}
