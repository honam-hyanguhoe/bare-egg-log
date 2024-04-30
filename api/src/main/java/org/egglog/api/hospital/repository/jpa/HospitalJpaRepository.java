package org.egglog.api.hospital.repository.jpa;


import org.egglog.api.hospital.model.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalJpaRepository extends JpaRepository<Hospital, Long>, HospitalQueryRepository {
}
