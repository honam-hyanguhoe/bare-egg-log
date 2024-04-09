package org.egglog.api.groupboard.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardListOutputSpec {
    private Long boardId;           // 게시판 아이디
    private String boardContent;    // 게시판 글 본문
    private String boardSubject;    // 게시판 글 제목
    private String boardCreatedAt;  // 게시판 작성 시간
    private Long hit;               // 게시판 조회수
    private Long commentCount;      // 댓글 갯수
    private Long boardLikeCount;    // 좋아요 수
    private String boardType;       // 게시판 타입 (투표, 일반, 근무표)
    private boolean doLiked;        // 좋아요 여부
    private boolean doHit;          // 조회 여부
    private boolean doComment;      // 댓글 작성 여부
}
