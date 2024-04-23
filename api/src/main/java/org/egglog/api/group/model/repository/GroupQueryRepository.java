package org.egglog.api.group.model.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GroupQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
}
