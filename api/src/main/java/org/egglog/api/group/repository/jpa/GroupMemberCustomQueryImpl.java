package org.egglog.api.group.repository.jpa;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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


@Repository
@RequiredArgsConstructor
public class GroupMemberCustomQueryImpl implements GroupMemberCustomQuery{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<GroupMemberDto> findGroupMemberByGroupId(Long groupId) {
        return jpaQueryFactory
                .select(Projections.fields(GroupMemberDto.class,
                        groupMember.user.id.as("userId"),
                        groupMember.group.id.as("groupId"),
                        groupMember.user.name.as("userName"),
                        groupMember.user.profileImgUrl.as("profileImgUrl"),
                        groupMember.isAdmin,
                        hospital.hospitalName.as("hospitalName")))
                .join(groupMember.user,user)
                    .on(groupMember.user.id.eq(user.id).and(groupMember.isAdmin.eq(false)))
                .join(groupMember.user.selectedHospital,hospital)
                    .on(groupMember.user.selectedHospital.id.eq(hospital.id))
                .fetch();
    }

    @Override
    public List<GroupMember> findGroupMemberAllByGroupId(Long groupId) {
        return jpaQueryFactory
                .selectFrom(groupMember)
                .where(groupMember.group.id.eq(groupId))
                .fetch();
    }

    @Override
    public Optional<GroupMemberDto> findGroupBossMemberByGroupId(Long groupId) {
        return Optional.ofNullable(jpaQueryFactory
                .select(Projections.fields(GroupMemberDto.class,
                        groupMember.user.id.as("userId"),
                        groupMember.group.id.as("groupId"),
                        groupMember.user.name.as("userName"),
                        groupMember.user.profileImgUrl.as("profileImgUrl"),
                        groupMember.isAdmin,
                        hospital.hospitalName.as("hospitalName")))
                .join(groupMember.user,user)
                .on(groupMember.user.id.eq(user.id).and(groupMember.isAdmin.eq(true)))
                .join(groupMember.user.selectedHospital,hospital)
                .on(groupMember.user.selectedHospital.id.eq(hospital.id))
                .fetchOne());
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
}
