package org.egglog.api.board.model.dto.params;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class CommentForm {

    @NotNull(message = "boardId는 필수 값 입니다.")
    @Positive(message = "정확한 ID 값을 입력 해주세요.")
    private Long boardId; //필수

    @NotBlank(message = "boardTitle는 필수 입니다.")
    private String commentContent; //필수

    @NotNull(message = "parentId는 필수 값 입니다.")
    @Positive(message = "정확한 ID 값을 입력 해주세요.")
    private Long parentId; //필수

    private String tempNickname;

}
