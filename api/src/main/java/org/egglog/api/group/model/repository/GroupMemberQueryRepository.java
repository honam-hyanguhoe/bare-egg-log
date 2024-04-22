package org.egglog.api.group.model.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.egglog.api.group.model.entity.GroupMember;
import org.springframework.stereotype.Repository;

import static org.egglog.api.group.model.entity.QGroupMember.groupMember;
import static org.egglog.api.user.model.entity.QUsers.users;

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
    public Optional<GroupMember> findGroupBossMemberByGroupId(Long groupId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(groupMember)
                .where(groupMember.groupId.eq(groupId).and(groupMember.isAdmin.isTrue()))
                .fetchOne());
    }
    public Optional<GroupMember> findGroupMemberByGroupIdAndUserId(Long groupId, Long userId){
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(groupMember)
                .where(groupMember.groupId.eq(groupId).and(groupMember.user.id.eq(userId)))
                .fetchOne()
        );
    }
}
