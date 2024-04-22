package org.egglog.api.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.user.model.entity.QUsers;
import org.egglog.api.user.model.entity.Users;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.egglog.api.user.model.entity.QUsers.users;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<Users> findById(Long userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(users)
                .where(users.id.eq(userId))
                .fetchOne());
    }


}
