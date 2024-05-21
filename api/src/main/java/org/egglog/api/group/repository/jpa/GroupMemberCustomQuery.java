package org.egglog.api.group.repository.jpa;

import com.querydsl.core.Tuple;
import org.egglog.api.group.model.dto.response.GroupDutySummary;
import org.egglog.api.group.model.dto.response.GroupMemberDto;
import org.egglog.api.group.model.entity.GroupMember;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface GroupMemberCustomQuery {
    List<GroupMemberDto> findGroupMemberByGroupId(Long groupId);

    GroupMemberDto findGroupBossMemberByGroupId(Long groupId);

    Optional<GroupMember> findGroupMemberByGroupIdAndUserId(Long groupId, Long userId);

    List<GroupMember> findGroupMemberAllByGroupId(Long groupId);

    Integer countGroupMember(Long groupId);

    GroupDutySummary getGroupDutySummary(Long groupId, LocalDate targetDate);

    List<Long> findGroupIdByUserId(Long id);
}

