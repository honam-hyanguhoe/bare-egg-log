package org.egglog.api.inquiry.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.egglog.api.inquiry.model.entity.InquiryType;

import java.time.LocalDateTime;

@Data
@Builder
public class InquiryRegistForm {
    private String email;
    @NotBlank(message = "title 는 필수입니다.")
    private String title;
    @NotBlank(message = "content 는 필수입니다.")
    private String content;
}
