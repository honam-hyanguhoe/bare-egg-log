package org.egglog.api.group.model.dto.request;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupForm {
    @NotNull(message = "groupImage 는 필수입니다.")
    private Integer groupImage; //필수
    @NotBlank(message = "groupPassword 는 필수입니다.")
    private String groupPassword; //필수
    @NotBlank(message = "groupName 는 필수입니다.")
    private String groupName; //필수
}
