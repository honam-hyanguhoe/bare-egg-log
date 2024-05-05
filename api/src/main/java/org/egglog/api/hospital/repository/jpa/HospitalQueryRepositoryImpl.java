package org.egglog.api.hospital.repository.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.egglog.api.hospital.model.entity.Hospital;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


import static org.egglog.api.hospital.model.entity.QHospital.hospital;


@Repository
@RequiredArgsConstructor
public class HospitalQueryRepositoryImpl implements HospitalQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<Hospital> findById(Long hospitalId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(hospital)
                .where(hospital.id.eq(hospitalId))
                .fetchOne());
    }

    public List<Hospital> findHospitals(String hospitalName, int offset, int limit) {
        var query = jpaQueryFactory
                .selectFrom(hospital);

        if (hospitalName != null && !hospitalName.isEmpty()) {
            query.where(hospital.hospitalName.containsIgnoreCase(hospitalName));
        }

        return query
                .orderBy(hospital.hospitalName.asc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }


}
