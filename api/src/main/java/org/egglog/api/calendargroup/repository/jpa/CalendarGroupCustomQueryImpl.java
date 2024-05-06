package org.egglog.api.calendargroup.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.calendargroup.model.entity.CalendarGroup;
import org.egglog.api.calendargroup.model.entity.QCalendarGroup;
import org.egglog.api.user.model.entity.QUser;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.egglog.api.calendargroup.model.entity.QCalendarGroup.calendarGroup;
import static org.egglog.api.user.model.entity.QUser.user;
@Repository
@RequiredArgsConstructor
public class CalendarGroupCustomQueryImpl implements CalendarGroupCustomQuery {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * CalendarGroup의 ID를 사용하여 CalendarGroup 엔티티와 연결된 User 정보를 함께 조회합니다.
     * @Creator 김형민
     * @param calendarGroupId 조회하고자 하는 CalendarGroup의 ID
     * @return CalendarGroup 엔티티, 이와 연결된 User 정보를 포함하여 반환
     */
    public Optional<CalendarGroup> findCalendarGroupWithUserById(Long calendarGroupId) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(calendarGroup)
                        .leftJoin(calendarGroup.user, user)  // User와 조인
                        .fetchJoin()  // 조인된 User 정보를 함께 가져오기
                        .where(calendarGroup.id.eq(calendarGroupId))
                        .fetchOne());  // 결과를 Optional로 감싸서 반환
    }

    /**
     * 사용자 ID를 기반으로 모든 CalendarGroup 목록을 조회합니다.
     * @param userId 조회하고자 하는 User의 ID
     * @return 해당 사용자의 모든 CalendarGroup 리스트
     * @Creator 김형민
     */
    public List<CalendarGroup> findListByUserId(Long userId) {
        return jpaQueryFactory
                .selectFrom(calendarGroup)
                .where(calendarGroup.user.id.eq(userId))
                .fetch();
    }

}
