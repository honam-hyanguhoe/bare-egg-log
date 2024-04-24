package org.egglog.api.user.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.model.entity.QUser;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.egglog.api.user.model.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(user)
                .where(user.id.eq(userId))
                .fetchOne());
    }

    public Optional<User> findByIdWithHospital(Long userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(user)
                .leftJoin(user.hospital).fetchJoin()
                .where(user.id.eq(userId))
                .fetchOne());
    }
    public Optional<User> findByEmailWithHospital(String email) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(user)
                .leftJoin(user.hospital).fetchJoin()
                .where(user.email.eq(email))
                .fetchOne());
    }

}
