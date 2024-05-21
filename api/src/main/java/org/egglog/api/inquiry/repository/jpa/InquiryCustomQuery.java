package org.egglog.api.inquiry.repository.jpa;

import org.egglog.api.group.model.dto.response.GroupPreviewDto;
import org.egglog.api.inquiry.model.dto.response.InquiryDto;

import java.util.List;
import java.util.Optional;

public interface InquiryCustomQuery {
    Optional<List<InquiryDto>> searchInquiryList(String noted, Long index);
    void setNotedTrue(Long inquiryId);
}
