package org.egglog.api.hospital.repository.jpa;

import org.egglog.api.hospital.model.entity.Hospital;
import org.egglog.api.hospital.model.entity.HospitalAuth;
import org.egglog.api.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HospitalAuthJpaRepository extends JpaRepository<HospitalAuth, Long>, HospitalAuthQueryRepository {

    Optional<HospitalAuth> findByUserAndHospital(User user, Hospital userSelectHospital);

}
