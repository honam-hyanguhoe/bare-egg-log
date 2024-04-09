package org.egglog.api.groupboard.model.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Comment {
    private Long commentId;
    private Long boardId;
    private Long userId;
    private String commentContent;
    private Long commentLevel;
    private int commentGroup;
    private String commentCreateAt;
}
