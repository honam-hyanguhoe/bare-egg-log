package org.egglog.api.inquiry.model.dto.request;

import lombok.Builder;
import lombok.Data;
import org.egglog.api.inquiry.model.entity.InquiryType;

import java.time.LocalDateTime;

@Data
@Builder
public class InquiryRegistForm {
    private String title;
    private String content;
}
