package org.egglog.api.user.model.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
/**
 * packageName    : org.egglog.api.user.model.dto.request
 * fileName       : UpdateUserRequest
 * author         : SSAFY
 * date           : 2024-04-24
 * description    : 업데이트 할 유저 정보 객체입니다.
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
public class UpdateUserRequest {
    private String userName;
    private String profileImgUrl;
}
