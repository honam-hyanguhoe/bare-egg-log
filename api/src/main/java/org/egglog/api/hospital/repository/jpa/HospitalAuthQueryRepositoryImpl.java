package org.egglog.api.hospital.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.hospital.model.entity.HospitalAuth;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import static org.egglog.api.hospital.model.entity.QHospitalAuth.hospitalAuth;
import static org.egglog.api.hospital.model.entity.QHospital.hospital;
import static org.egglog.api.user.model.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class HospitalAuthQueryRepositoryImpl implements HospitalAuthQueryRepository{
    private final JPAQueryFactory jpaQueryFactory;

    public Optional<HospitalAuth> findByIdWithHospitalAndUser(Long hospitalAuthId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(hospitalAuth)
                .leftJoin(hospitalAuth.hospital, hospital).fetchJoin()
                .leftJoin(hospitalAuth.user, user).fetchJoin()
                .where(hospitalAuth.id.eq(hospitalAuthId))
                .fetchOne());
    }

    public Optional<HospitalAuth> findByUserIdAndHospitalIdWithHospitalAndUser(Long userId, Long hospitalId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(hospitalAuth)
                .leftJoin(hospitalAuth.hospital, hospital).fetchJoin()
                .leftJoin(hospitalAuth.user, user).fetchJoin()
                .where(hospitalAuth.user.id.eq(userId).and(hospitalAuth.hospital.id.eq(hospitalId)))
                .fetchOne());
    }

    public List<HospitalAuth> findListByUserIdWithHospital(Long userId) {
        return jpaQueryFactory
                .selectFrom(hospitalAuth)
                .leftJoin(hospitalAuth.hospital, hospital).fetchJoin()
                .where(hospitalAuth.user.id.eq(userId))
                .fetch();
    }

    public List<HospitalAuth> findListByHospitalIdWithUser(Long hospitalId) {
        return jpaQueryFactory
                .selectFrom(hospitalAuth)
                .leftJoin(hospitalAuth.user, user).fetchJoin()
                .where(hospitalAuth.hospital.id.eq(hospitalId))
                .fetch();
    }
}
