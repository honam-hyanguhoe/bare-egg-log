package org.egglog.api.groupboard.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VoteUserOutputSpec {
    private Long voteContentId;
    private String voteContent;
    private List<VoteUserInfoOutputSpec> userInfoList;
}
