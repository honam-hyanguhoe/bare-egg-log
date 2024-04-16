package org.egglog.api.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.user.model.entity.Users;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

//    public Optional<Users> findMemberById(Long userId) {
//        return Optional.ofNullable(jpaQueryFactory
//                .selectFrom(us)
//                .where(member.id.eq(memberId))
//                .fetchOne());
//    }

}
