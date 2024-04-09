package org.egglog.api.groupboard.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RecommentOutputSpec {
    private Long commentId;
    private Long userId;
    private String hospitalName;
    private String userName;
    private String commentCreateAt;
    private String commentContent;
    private Long commentGroup;
    private String profileImgUrl;
}
