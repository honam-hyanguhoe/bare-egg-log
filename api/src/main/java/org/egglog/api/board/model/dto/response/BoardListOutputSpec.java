package org.egglog.api.board.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.egglog.api.board.model.entity.Board;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class BoardListOutputSpec {

    private Long boardId;           // 게시판 아이디

    private String boardTitle;    // 게시판 글 제목

    private String boardContent;    // 게시판 글 본문

    private LocalDateTime boardCreatedAt;  // 게시판 작성 시간

    private String tempNickname;

    private long viewCount;         // 게시판 조회수

    private long commentCount;      // 댓글 갯수

    private long likeCount;    // 좋아요 수

    private String pictureOne;  // 첫번째 사진

    private Boolean isLiked;        // 좋아요 여부

    private Boolean isCommented;    //댓글 유무 여부
    
    private Boolean isHospitalAuth;     // 인증 여부

    private String hospitalName;    // 사용자 병원명

    private Long userId;            // 유저 id

    public BoardListOutputSpec(Board board, long commentCount, long likeCount, String hospitalName, Boolean isHospitalAuth) {

        this.boardId = board.getId();
        this.boardTitle = board.getTitle();
        this.boardContent = board.getContent();
        this.boardCreatedAt = board.getCreatedAt();
        this.tempNickname = board.getTempNickname();
        this.commentCount = commentCount;
        this.viewCount = board.getViewCount();
        this.likeCount = likeCount;
        this.pictureOne = board.getPictureOne();
        this.userId = board.getUser().getId();
        this.hospitalName = hospitalName;
        this.isHospitalAuth = isHospitalAuth;
    }

}
