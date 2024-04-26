package org.egglog.api.user.model.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

/**
 * packageName    : org.egglog.api.user.model.dto.request
 * fileName       : JoinUserRequest
 * author         : SSAFY
 * date           : 2024-04-24
 * description    : 추가 정보 입력을 받아 로그인을 완료하는 정보 객체입니다.
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-24        김형민       최초 생성
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JoinUserRequest {
    private String userName;
    private Long hospitalId;
    private String empNo;
    private String fcmToken;
}
