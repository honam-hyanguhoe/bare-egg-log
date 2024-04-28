package org.egglog.api.inquiry.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class InquiryUserDto {
    private Long userId;
    private String userName;
    private String profileImage;
    private String userEmail;
}
