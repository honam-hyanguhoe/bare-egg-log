package org.egglog.api.user.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.user.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.egglog.api.user.model.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryImpl implements UserQueryRepository{

    private final JPAQueryFactory jpaQueryFactory;


    public Optional<User> findByIdWithHospital(Long userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(user)
                .leftJoin(user.selectedHospital).fetchJoin()
                .where(user.id.eq(userId))
                .fetchOne());
    }
    public Optional<User> findByEmailWithHospital(String email) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(user)
                .leftJoin(user.selectedHospital).fetchJoin()
                .where(user.email.eq(email))
                .fetchOne());
    }

}
