package org.egglog.api.group.repository.jpa;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.group.model.dto.response.GroupPreviewDto;
import org.egglog.api.group.model.entity.Group;
import org.egglog.api.group.model.entity.QGroupMember;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.egglog.api.group.model.entity.QGroup.group;
import static org.egglog.api.group.model.entity.QGroupMember.groupMember;
import static org.egglog.api.user.model.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class GroupCustomQueryImpl implements GroupCustomQuery{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<GroupPreviewDto> findGroupByUserId(Long userId){
        QGroupMember adminGroupMember = new QGroupMember("adminGroupMember");
        return jpaQueryFactory
                .select(Projections.constructor(GroupPreviewDto.class,
                        group.id,
                        group.groupImage,
                        group.groupName,
                        user.name.as("admin"),
                        groupMember.id.count().as("memberCount")
                        ))
                .from(group)
                .join(groupMember).on(group.id.eq(groupMember.group.id))
                .leftJoin(adminGroupMember).on(group.id.eq(adminGroupMember.group.id).and(adminGroupMember.isAdmin.eq(true)))
                .leftJoin(user).on(adminGroupMember.user.id.eq(user.id))
                .where(group.id.in(
                        JPAExpressions
                                .selectDistinct(groupMember.group.id)
                                .from(groupMember)
                                .where(groupMember.user.id.eq(userId))
                ))
                .groupBy(group.id, group.groupName, group.groupImage, user.id, user.name)
                .fetch();
    }

}
