package org.egglog.api.group.model.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.group.model.entity.GroupMember;
import org.springframework.stereotype.Repository;

import static org.egglog.api.group.model.entity.QGroupMember.groupMember;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class GroupMemberCustomQueryImpl implements GroupMemberCustomQuery{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<GroupMember>> findGroupMemberByGroupId(Long groupId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(groupMember)
                .where(groupMember.group.id.eq(groupId).and(groupMember.isAdmin.isFalse()))
                .fetch());
    }
    @Override
    public Optional<GroupMember> findGroupBossMemberByGroupId(Long groupId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(groupMember)
                .where(groupMember.group.id.eq(groupId).and(groupMember.isAdmin.isTrue()))
                .fetchOne());
    }
    @Override
    public Optional<GroupMember> findGroupMemberByGroupIdAndUserId(Long groupId, Long userId){
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(groupMember)
                .where(groupMember.group.id.eq(groupId).and(groupMember.user.id.eq(userId)))
                .fetchOne()
        );
    }

    @Override
    public Integer countGroupMember(Long groupId){
        Long count = jpaQueryFactory
                .select(groupMember.count())
                .from(groupMember)
                .where(groupMember.group.id.eq(groupId))
                .fetchOne();
        if(count == null){
            return 0;
        }else{
            return count.intValue();
        }
    }

}
