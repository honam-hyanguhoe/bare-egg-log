package org.egglog.api.board.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class BoardOutputSpec {

    private Long boardId;           // 게시판 아이디

    private String boardTitle;    // 게시판 글 제목

    private String boardContent;    // 게시판 글 본문

    private LocalDateTime boardCreatedAt;  // 게시판 작성 시간

    private String pictureOne;      // 게시판 사진 1

    private String pictureTwo;      // 게시판 사진 2

    private String pictureThree;    // 게시판 사진 3

    private String pictureFour;     // 게시판 사진 4

    private Long groupId;           // 그룹 id

    private Long hospitalId;        //병원 id

    private Long userId;            // 작성자 id

    private String tempNickname;       // 익명 닉네임

    private String profileImgUrl;   // 작성자 프로필 사진

    private long commentCount;      // 댓글 개수

    private long viewCount;     //조회수

    private long boardLikeCount;    // 좋아요 수

    private String hospitalName;    // 작성자 병원명

    private Boolean isLiked;        // 좋아요 여부

    private Boolean isCommented;        // 댓글 여부

    private Boolean isHospitalAuth;     // 인증 여부

}
