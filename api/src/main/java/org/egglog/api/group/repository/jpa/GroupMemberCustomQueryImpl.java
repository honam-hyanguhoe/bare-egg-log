package org.egglog.api.group.repository.jpa;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.group.exception.GroupMemberErrorCode;
import org.egglog.api.group.exception.GroupMemberException;
import org.egglog.api.group.model.dto.response.GroupDutySummary;
import org.egglog.api.group.model.dto.response.GroupMemberDto;
import org.egglog.api.group.model.dto.response.GroupMemberSimpleDto;
import org.egglog.api.group.model.entity.GroupMember;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.worktype.model.entity.WorkTag;
import org.springframework.stereotype.Repository;

import static org.egglog.api.hospital.model.entity.QHospital.hospital;
import static org.egglog.api.group.model.entity.QGroupMember.groupMember;
import static org.egglog.api.user.model.entity.QUser.user;
import static org.egglog.api.work.model.entity.QWork.work;
import static org.egglog.api.worktype.model.entity.QWorkType.workType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class GroupMemberCustomQueryImpl implements GroupMemberCustomQuery{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GroupMemberDto> findGroupMemberByGroupId(Long groupId) {
        List<GroupMember> results = jpaQueryFactory
                .select(groupMember)
                .from(groupMember)
                .join(groupMember.user, user).fetchJoin()
                .join(groupMember.user.selectedHospital, hospital).fetchJoin()
                .where(groupMember.group.id.eq(groupId)
                        .and(groupMember.isAdmin.eq(false))) // 그룹 ID로 필터링 추가
                .fetch();

        return results.stream()
                .map(GroupMember::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupMember> findGroupMemberAllByGroupId(Long groupId) {
        return jpaQueryFactory
                .selectFrom(groupMember)
                .where(groupMember.group.id.eq(groupId))
                .fetch();
    }

    @Override
    public GroupMemberDto findGroupBossMemberByGroupId(Long groupId) {
        GroupMember boss = Optional.ofNullable(jpaQueryFactory
                .select(groupMember)
                .from(groupMember)
                .join(groupMember.user, user).fetchJoin()
                .join(groupMember.user.selectedHospital, hospital).fetchJoin()
                .where(groupMember.group.id.eq(groupId)
                        .and(groupMember.isAdmin.eq(true))) // 그룹 ID로 필터링 추가
                .fetchOne()).orElseThrow(() -> new GroupMemberException(GroupMemberErrorCode.NOT_FOUND));
        return boss.toDto();
    }
    @Override
    public Optional<GroupMember> findGroupMemberByGroupIdAndUserId(Long groupId, Long userId){
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(groupMember)
                .leftJoin(groupMember.user).fetchJoin()
                .leftJoin(groupMember.group).fetchJoin()
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

    @Override
    public GroupDutySummary getGroupDutySummary(Long groupId, LocalDate targetDate){

        List<Tuple> results = jpaQueryFactory
                .select(workType, user)
                .from(groupMember)
                .join(user).on(user.id.eq(groupMember.user.id))  // GroupMember와 User를 userId로 조인합니다.
                .join(work).on(work.user.id.eq(user.id))  // User와 Work를 userId로 조인합니다.
                .join(workType).on(workType.id.eq(work.workType.id))  // Work와 WorkType을 workTypeId로 조인합니다.
                .where(groupMember.group.id.eq(groupId),  // group_id가 'n'인 GroupMember를 검색
                        work.workDate.eq(targetDate))  // workDate가 2024-04-24인 Work를 검색
                .fetch();

        GroupDutySummary groupDutySummary = new GroupDutySummary();
        GroupMemberSimpleDto groupMemberDto = null;
        // 결과 처리
        for (Tuple result : results) {
            User fetchedUser = result.get(user);
            WorkTag fetchedWorkType = result.get(workType).getWorkTag();

            groupMemberDto = new GroupMemberSimpleDto();
            groupMemberDto.setProfileImgUrl(fetchedUser.getProfileImgUrl());
            groupMemberDto.setUserName(fetchedUser.getName());
            groupMemberDto.setUserId(fetchedUser.getId());

            if(fetchedWorkType==WorkTag.DAY){
                groupDutySummary.day.add(groupMemberDto);
            }else if(fetchedWorkType==WorkTag.EVE){
                groupDutySummary.eve.add(groupMemberDto);
            }else if(fetchedWorkType==WorkTag.NIGHT){
                groupDutySummary.night.add(groupMemberDto);
            }else if(fetchedWorkType==WorkTag.OFF){
                groupDutySummary.off.add(groupMemberDto);
            }else{
                groupDutySummary.etc.add(groupMemberDto);
            }
        }
        return groupDutySummary;
    }

    @Override
    public List<Long> findGroupIdByUserId(Long userId) {
        return jpaQueryFactory
                .selectDistinct(groupMember.group.id)
                .from(groupMember)
                .where(groupMember.user.id.eq(userId))
                .fetch();
    }
}
