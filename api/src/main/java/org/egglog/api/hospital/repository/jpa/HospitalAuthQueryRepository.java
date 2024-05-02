package org.egglog.api.hospital.repository.jpa;

import org.egglog.api.hospital.model.entity.HospitalAuth;

import java.util.List;
import java.util.Optional;

public interface HospitalAuthQueryRepository {
    Optional<HospitalAuth> findByIdWithHospitalAndUser(Long hospitalAuthId);
    Optional<HospitalAuth> findByUserIdAndHospitalIdWithHospitalAndUser(Long userId, Long hospitalId);
    List<HospitalAuth> findListByUserIdWithHospital(Long userId);
    List<HospitalAuth> findListByHospitalIdWithUser(Long hospitalId);
    List<HospitalAuth> findAuthListWithUser(Boolean authType);

}
