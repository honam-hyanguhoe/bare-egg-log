package org.egglog.api.group.model.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvitationAcceptForm {
    @NotBlank(message = "invitationCode 는 필수입니다.")
    private String invitationCode; //필수
    @NotBlank(message = "password 는 필수입니다.")
    private String password; //필수
}
