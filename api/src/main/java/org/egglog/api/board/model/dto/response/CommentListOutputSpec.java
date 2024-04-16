package org.egglog.api.board.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentListOutputSpec {
    private Long commentId;
    private Long userId;
    private String hospitalName;
    private String userName;
    private String commentCreateAt;
    private String commentContent;
    private Long commentGroup;
    private String profileImgUrl;
    private List<RecommentOutputSpec> recomment;
}
