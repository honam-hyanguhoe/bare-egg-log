package org.egglog.api.groupboard.model.dto.params;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class VoteData {
    private String voteName;
    private boolean voteAnonymous;
    private List<String> voteContent;
}
