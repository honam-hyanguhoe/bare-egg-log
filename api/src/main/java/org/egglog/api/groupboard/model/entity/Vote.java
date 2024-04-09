package org.egglog.api.groupboard.model.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Vote {
    private Long voteId;
    private Long boardId;
    private boolean voteAnonymous;
    private String voteName;
}
