package org.egglog.api.board.model.dto.params;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class BoardForm {
    @NotBlank(message = "boardTitle 는 필수 입니다.")
    private String boardTitle; //필수
    @NotBlank(message = "boardContent 는 필수 입니다.")
    private String boardContent; //필수

    private String pictureOne;

    private String pictureTwo;

    private String pictureThree;

    private String pictureFour;

    private String tempNickname;  //익명 닉네임

    private Long groupId;  //그룹 아이디
    
    private Long hospitalId;  //병원 아이디
}
