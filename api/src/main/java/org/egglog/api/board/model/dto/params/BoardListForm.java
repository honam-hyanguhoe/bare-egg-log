package org.egglog.api.board.model.dto.params;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class BoardListForm {

    private Long hospitalId;    //병원 아이디

    private Long groupId;   //그룹 아이디

    private String searchWord;  //검색어

    @NotNull(message = "offset은 필수 입니다.")
    private Long offset; //필수

}
