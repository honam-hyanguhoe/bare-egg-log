package org.egglog.api.board.model.dto.params;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentForm {

    private Long boardId;

    private String commentContent;

    private Long parentId;

    private int commentDepth;

    private String tempNickname;

}
