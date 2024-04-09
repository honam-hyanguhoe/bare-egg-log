package org.egglog.api.groupboard.model.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class BoardLike {
    private Long boardId;
    private Long userId;
}
