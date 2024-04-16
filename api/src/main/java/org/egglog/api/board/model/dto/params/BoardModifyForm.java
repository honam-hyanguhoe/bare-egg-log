package org.egglog.api.board.model.dto.params;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardModifyForm {

    private Long boardId;

    private String boardContent;

    private String boardSubject;

    private String pictureOne;

    private String pictureTwo;

    private String pictureThree;

    private String pictureFour;
}
