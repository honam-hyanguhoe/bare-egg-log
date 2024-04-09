package org.egglog.api.groupboard.model.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class VoteUser {
    private Long voteContentId;
    private Long voteId;
    private Long userId;
}
