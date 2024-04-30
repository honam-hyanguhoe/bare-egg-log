package org.egglog.api.worktype.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.worktype.model.entity.WorkType;
import org.springframework.stereotype.Repository;

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
public class WorkTypeQueryRepositoryImpl implements WorkTypeQueryRepository{
    private final JPAQueryFactory jpaQueryFactory;

}
