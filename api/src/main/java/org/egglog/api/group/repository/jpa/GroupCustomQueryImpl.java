package org.egglog.api.group.repository.jpa;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.group.model.dto.response.GroupPreviewDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.egglog.api.group.model.entity.QGroup.group;
import static org.egglog.api.group.model.entity.QGroupMember.groupMember;

@Repository
@RequiredArgsConstructor
public class GroupCustomQueryImpl implements GroupCustomQuery{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Optional<List<GroupPreviewDto>> findGroupByUserId(Long userId){
        List<Tuple> results = jpaQueryFactory
                .select(group.groupImage, group.groupName, group.id, group.admin, Expressions.as(groupMember.count(),"memberCount"))
                .from(groupMember)
                .leftJoin(groupMember.group, group)
                .where(groupMember.user.id.eq(userId))
                .fetch();

        List<GroupPreviewDto> dtos = results.stream().map(tuple -> {
            NumberPath<Long> memberCountPath = Expressions.numberPath(Long.class, "memberCount");
            Integer memberCount = 0;
            if(tuple.get(memberCountPath)!=null){
                memberCount=tuple.get(memberCountPath).intValue();
            }
            return GroupPreviewDto.builder()
                    .groupImage(tuple.get(group.groupImage))
                    .groupName(tuple.get(group.groupName))
                    .groupId(tuple.get(group.id))
                    .admin(tuple.get(group.admin))
                    .memberCount(memberCount)
                    .build();
        }).collect(Collectors.toList());

        return Optional.ofNullable(dtos.isEmpty() ? null : dtos);
    }
}
