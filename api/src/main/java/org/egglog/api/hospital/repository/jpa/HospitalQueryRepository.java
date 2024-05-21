package org.egglog.api.hospital.repository.jpa;

import org.egglog.api.hospital.model.entity.Hospital;

import java.util.List;
import java.util.Optional;

public interface HospitalQueryRepository {
    List<Hospital> findHospitals(String hospitalName, int offset, int limit);
}
