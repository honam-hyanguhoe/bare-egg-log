package org.egglog.api.inquiry.model.dto.response;

import lombok.Builder;
import lombok.Data;
import org.egglog.api.inquiry.model.entity.InquiryType;

@Data
@Builder
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InquiryDto {
    private Long id;
    private String title;
    private String content;
    private InquiryType inquiryType;
    private Boolean isNoted;
    private InquiryUserDto user;
}
