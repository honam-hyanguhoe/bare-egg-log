package org.egglog.api.group.model.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.egglog.api.group.model.entity.GroupMember;
import org.springframework.stereotype.Repository;

import static org.egglog.api.group.model.entity.QGroupMember.groupMember;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class GroupMemberQueryRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public Optional<List<GroupMember>> findGroupMemberByGroupId(Long groupId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(groupMember)
                .where(groupMember.groupId.eq(groupId).and(groupMember.isAdmin.isFalse()))
                .fetch());
    }
    public Optional<List<GroupMember>> findGroupBossMemberByGroupId(Long groupId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(groupMember)
                .where(groupMember.groupId.eq(groupId).and(groupMember.isAdmin.isTrue()))
                .fetch());
    }
}
