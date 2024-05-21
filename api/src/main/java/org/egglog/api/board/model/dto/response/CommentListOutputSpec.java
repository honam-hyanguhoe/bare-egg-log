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
public class CommentListOutputSpec {

    private Long commentId;

    private String commentContent;

    private String hospitalName;

    private Long userId;

    private String tempNickname;

    private LocalDateTime commentCreateAt;

    private String profileImgUrl;

    private Boolean isHospitalAuth;     // 인증 여부

    private List<RecommentOutputSpec> recomments;    //대댓글
}
