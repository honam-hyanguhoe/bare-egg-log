package org.egglog.api.board.model.dto.params;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class LikeForm {
    @NotNull(message = "boardId는 필수 값 입니다.")
    @Positive(message = "정확한 ID 값을 입력 해주세요.")
    private Long boardId;//필수
}
