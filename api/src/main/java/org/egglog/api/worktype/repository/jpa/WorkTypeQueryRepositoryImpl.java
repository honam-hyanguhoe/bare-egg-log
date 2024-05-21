package org.egglog.api.worktype.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.user.model.entity.QUser;
import org.egglog.api.worktype.model.entity.QWorkType;
import org.egglog.api.worktype.model.entity.WorkTag;
import org.egglog.api.worktype.model.entity.WorkType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.egglog.api.user.model.entity.QUser.user;
import static org.egglog.api.worktype.model.entity.QWorkType.workType;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.worktype.repository.jpa
 * fileName      : WorkTypeQueryRepositoryImpl
 * description    : 근무 타입 쿼리 dsl 구현 레포지토리입니다.
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-30|김형민|최초 생성|
 */
@Repository
@RequiredArgsConstructor
public class WorkTypeQueryRepositoryImpl implements WorkTypeQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<WorkType> findListByUserId(Long userId) {
        return jpaQueryFactory
                .selectFrom(workType)
                .where(workType.user.id.eq(userId).and(workType.workTag.ne(WorkTag.DELETE)))
                .fetch();
    }


    @Override
    public Optional<WorkType> findWithUserById(Long workTypeId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(workType)
                .leftJoin(workType.user, user).fetchJoin()
                .where(workType.id.eq(workTypeId))
                .fetchOne());
    }

    @Override
    public List<WorkType> findMainWorkTypesByUserId(Long userId) {
        return jpaQueryFactory
                .selectFrom(workType)
                .where(workType.user.id.eq(userId).and(workType.workTag.in(WorkTag.DAY,WorkTag.EVE,WorkTag.NIGHT,WorkTag.OFF)))
                .fetch();
    }
}
