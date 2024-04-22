package org.egglog.api.hospital.model.repository;

import org.egglog.api.board.model.entity.Board;
import org.egglog.api.hospital.model.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalJpaRepository extends JpaRepository<Hospital, Long> {
}
