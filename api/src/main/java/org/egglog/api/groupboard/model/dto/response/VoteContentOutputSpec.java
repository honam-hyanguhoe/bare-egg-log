package org.egglog.api.groupboard.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VoteContentOutputSpec {
    private Long voteContentId;
    private String voteContent;
    private Long voteUserCount;
    private boolean doSelected;
}
