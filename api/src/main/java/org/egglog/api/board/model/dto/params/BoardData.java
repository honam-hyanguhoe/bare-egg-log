package org.egglog.api.board.model.dto.params;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.egglog.api.board.model.entity.BoardType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class BoardData {
    @NotBlank(message = "boardContent 는 필수 입니다.")
    private String boardContent; //필수
    @NotBlank(message = "boardSubject 는 필수 입니다.")
    private String boardSubject;  //필수

    private Long groupId;

    private BoardType boardType;

    private String pictureOne;

    private String pictureTwo;

    private String pictureThree;

    private String pictureFour;
}
