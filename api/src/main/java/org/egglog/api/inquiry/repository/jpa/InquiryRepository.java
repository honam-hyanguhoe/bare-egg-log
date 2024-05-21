package org.egglog.api.inquiry.repository.jpa;

import org.egglog.api.inquiry.model.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>, InquiryCustomQuery {
}
