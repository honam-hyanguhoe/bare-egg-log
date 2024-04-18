package org.egglog.api.board.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardOutputSpec {

    private Long boardId;           // 게시판 아이디

    private String boardTitle;    // 게시판 글 제목

    private String boardContent;    // 게시판 글 본문

    private LocalDateTime boardCreatedAt;  // 게시판 작성 시간

    private String pictureOne;      // 게시판 사진 1

    private String pictureTwo;      // 게시판 사진 2

    private String pictureThree;    // 게시판 사진 3

    private String pictureFour;     // 게시판 사진 4

    private Long hit;               // 게시판 조회수

    private Long groupId;           // 그룹 id

    private Long userId;            // 유저 id

    private String userName;        // 유저 이름

    private String profileImgUrl;   // 유저 프로필 사진

    private Long commentCount;      // 댓글 갯수

    private Long boardLikeCount;    // 좋아요 수

    private String hospitalName;    // 사용자 병원명

    private boolean doLiked;        // 좋아요 여부

    private boolean doHit;          // 조회 여부

}
