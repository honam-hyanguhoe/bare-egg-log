package org.egglog.api.work.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


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
}
