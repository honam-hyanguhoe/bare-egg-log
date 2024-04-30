package org.egglog.api.work.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.egglog.api.work.model.entity.QWork;
import org.egglog.api.user.model.entity.QUser;
import org.egglog.api.calendargroup.model.entity.QCalendarGroup;
import org.egglog.api.worktype.model.entity.QWorkType;
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
public class WorkQueryRepository {
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
    public List<Work> findWorkListAllByTime(Long calendarGroupId, LocalDate startDate, LocalDate endDate){
        return jpaQueryFactory
                .selectFrom(work)
                .leftJoin(work.workType, workType).fetchJoin() // WorkType과 함께 조인
                .where(work.calendarGroup.id.eq(calendarGroupId) // 캘린더 그룹 ID 필터
                        .and(work.workDate.between(startDate, endDate))) // 날짜 범위 필터
                .fetch();
    }

}
