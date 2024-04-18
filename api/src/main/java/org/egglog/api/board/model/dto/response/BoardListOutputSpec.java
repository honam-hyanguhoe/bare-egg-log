package org.egglog.api.board.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardListOutputSpec {

    private Long boardId;           // 게시판 아이디

    private String boardTitle;    // 게시판 글 제목

    private String boardContent;    // 게시판 글 본문

    private LocalDateTime boardCreatedAt;  // 게시판 작성 시간

    private Long hit;               // 게시판 조회수

    private Long commentCount;      // 댓글 갯수

    private Long boardLikeCount;    // 좋아요 수

    private boolean doLiked;        // 좋아요 여부

    private boolean doHit;          // 조회 여부

}
