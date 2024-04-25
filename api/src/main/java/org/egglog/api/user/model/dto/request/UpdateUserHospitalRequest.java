package org.egglog.api.user.model.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
/**
 * packageName    : org.egglog.api.user.model.dto.request
 * fileName       : UpdateUserHospitalRequest
 * author         : SSAFY
 * date           : 2024-04-24
 * description    : 유저의 병원 정보를 업데이트하는 정보 객체입니다.
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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UpdateUserHospitalRequest {
    private Long hospitalId;
    private String empNo;
}
