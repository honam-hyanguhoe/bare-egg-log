package org.egglog.api.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BoardHitQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;


}