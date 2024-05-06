package org.egglog.api.work.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.work.model.dto.response.upcoming.UpComingCountWorkResponse;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.Projections;
import org.egglog.api.work.model.entity.Work;

import static org.egglog.api.work.model.entity.QWork.work;
import static org.egglog.api.user.model.entity.QUser.user;
import static org.egglog.api.calendargroup.model.entity.QCalendarGroup.calendarGroup;
import static org.egglog.api.worktype.model.entity.QWorkType.workType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.work.repository.jpa
 * fileName      : WorkQueryRepository
 * description    : 근무 일정 QueryDsl 레포지토리
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-29|김형민|최초 생성|
 */
@Repository
@RequiredArgsConstructor
public class WorkQueryRepositoryImpl implements WorkQueryRepository{
    private final JPAQueryFactory jpaQueryFactory;

    /**
     * Work ID를 사용하여 Work 엔티티와 연결된 User 정보를 함께 조회합니다.
     * @Creator 김형민
     * @param workId 조회하고자 하는 Work의 ID
     * @return Work 엔티티, 이와 연결된 User 정보를 포함하여 반환
     */
    public Optional<Work> findWorkWithUserByWorkId(Long workId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(work)
                .leftJoin(work.user, user)
                .fetchJoin()
                .where(work.id.eq(workId))
                .fetchOne());
    }

    public Optional<Work> findWorkWithAllByWorkId(Long workId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(work)
                .leftJoin(work.user, user).fetchJoin()
                .leftJoin(work.calendarGroup, calendarGroup).fetchJoin()
                .leftJoin(work.workType, workType).fetchJoin()
                .where(work.id.eq(workId))
                .fetchOne());
    }

    public Optional<Work> findWorkWithCalendarGroupByWorkId(Long workId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(work)
                .leftJoin(work.calendarGroup, calendarGroup).fetchJoin()
                .where(work.id.eq(workId))
                .fetchOne());
    }

    public Optional<Work> findWorkWithWorkTypeByWorkId(Long workId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(work)
                .leftJoin(work.workType, workType).fetchJoin()
                .where(work.id.eq(workId))
                .fetchOne());
    }
    /**
     * 특정 캘린더 그룹의 일정 날짜 범위 내의 근무 일정을 조회합니다.
     * @param calendarGroupId 캘린더 그룹 ID
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 조회된 근무 일정 목록
     */
    public List<Work> findWorkListWithAllByTime(Long calendarGroupId, LocalDate startDate, LocalDate endDate){
        return jpaQueryFactory
                .selectFrom(work)
                .leftJoin(work.workType, workType).fetchJoin() // WorkType과 함께 조인
                .leftJoin(work.calendarGroup, calendarGroup).fetchJoin() // CalendarGroup도 함께 조인
                .where(work.calendarGroup.id.eq(calendarGroupId) // 캘린더 그룹 ID 필터
                        .and(work.workDate.between(startDate, endDate))) // 날짜 범위 필터
                .fetch();
    }

    public List<Work> findWorkListWithWorkTypeByTimeAndTargetUser(Long targetUserId, LocalDate startDate, LocalDate endDate){
        return jpaQueryFactory
                .selectFrom(work)
                .leftJoin(work.workType, workType).fetchJoin() // WorkType과 함께 조인
                .where(work.user.id.eq(targetUserId)
                        .and(work.workDate.between(startDate, endDate))) // 날짜 범위 필터
                .fetch();
    }

    /**
     * 사용자의 모든 일정 조회
     * @param userId
     * @return
     */
    public List<Work> findAllWorkWithWorkTypeByUser(Long userId) {
        return jpaQueryFactory
                .selectFrom(work)
                .leftJoin(work.workType, workType).fetchJoin()
                .where(work.user.id.eq(userId))
                .fetch();
    }

    public List<UpComingCountWorkResponse> findUpComingCountWork(Long userId, LocalDate today, LocalDate startDate, LocalDate endDate){
        return jpaQueryFactory
                .select(Projections.constructor(UpComingCountWorkResponse.class,
                        workType.title.as("name"),
                        work.id.count().as("value"),
                        workType.color))
                .from(work)
                .join(work.workType, workType)
                .where(work.user.id.eq(userId)
                        .and(work.workDate.between(startDate, endDate))
                        .and(work.workDate.goe(today)))
                .groupBy(workType.title, workType.color)
                .fetch();
    }

    public List<Work> findWorksBeforeDate(Long userId, LocalDate today, LocalDate targetMonth) {
        // month의 첫날과 마지막날 계산
        LocalDate startOfMonth = targetMonth.withDayOfMonth(1);
        LocalDate endOfMonth = targetMonth.withDayOfMonth(targetMonth.lengthOfMonth());

        // 조건 설정: today가 month의 달에 포함되는지 확인
        if (today.getMonth() == targetMonth.getMonth() && today.getYear() == targetMonth.getYear()) endOfMonth = today;


        return jpaQueryFactory
                .selectFrom(work)
                .leftJoin(work.workType, workType).fetchJoin() // WorkType과 함께 조인
                .where(work.user.id.eq(userId)
                        .and(work.workDate.goe(startOfMonth))
                        .and(work.workDate.loe(endOfMonth)))
                .fetch();
    }
}
