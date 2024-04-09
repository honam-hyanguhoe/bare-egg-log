package org.egglog.api.groupboard.model.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Board {
    private Long boardId;
    private String boardContent;
    private String boardSubject;
    private String boardCreateAt;
    private String pictureOne;
    private String pictureTwo;
    private String pictureThree;
    private String pictureFour;
    private String boardType;
    private Long groupId;
    private Long userId;
}
