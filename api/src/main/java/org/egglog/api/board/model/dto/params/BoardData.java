package org.egglog.api.board.model.dto.params;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.egglog.api.board.model.entity.BoardType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class BoardData {

    private String boardContent;

    private String boardSubject;

    private Long groupId;

    private BoardType boardType;

    private String pictureOne;

    private String pictureTwo;

    private String pictureThree;

    private String pictureFour;
}
