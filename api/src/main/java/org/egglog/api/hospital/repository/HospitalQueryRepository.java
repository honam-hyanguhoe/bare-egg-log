package org.egglog.api.hospital.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.board.model.entity.Board;
import org.egglog.api.hospital.model.entity.Hospital;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static org.egglog.api.hospital.model.entity.QHospital.hospital;


@Repository
@RequiredArgsConstructor
public class HospitalQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<Hospital> findById(Long hospitalId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(hospital)
                .where(hospital.id.eq(hospitalId))
                .fetchOne());
    }

}
