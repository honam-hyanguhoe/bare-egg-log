package org.egglog.api.inquiry.model.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InquiryUserDto {
    private Long userId;
    private String userName;
    private String profileImage;
    private String userEmail;
}
