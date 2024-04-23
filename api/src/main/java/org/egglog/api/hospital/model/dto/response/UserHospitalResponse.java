package org.egglog.api.hospital.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

/**
 * packageName    : org.egglog.api.hospital.model.dto.response
 * fileName       : HospitalResponse
 * author         : 김형민
 * date           : 2024-04-22
 * description    : 병원 정보를 리턴하는 객체입니다.
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-22        김형민       최초 생성
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserHospitalResponse {

    private Long id;
    private String sidoCode;
    private String sido;
    private String gunguCode;
    private String gungu;
    private String dong;
    private String zipCode;
    private String address;
    private String hospitalName;
    private String lat;
    private String lng;

}
