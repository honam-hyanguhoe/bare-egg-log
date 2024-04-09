package org.egglog.api.security.repository;

import com.nursetest.app.security.model.entity.CertificationNumber;
import org.springframework.data.repository.CrudRepository;

public interface EmailCertificationRepository extends CrudRepository<CertificationNumber, String> {

}
