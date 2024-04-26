package org.egglog.api.hospital.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.hospital.model.entity.Hospital;
import org.egglog.api.user.model.entity.User;

import java.time.LocalDateTime;

/**
 * packageName    : org.egglog.api.hospital.model.dto.response
 * fileName       : HospitalAuthResponse
 * author         : 김형민
 * date           : 2024-04-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-26        김형민       최초 생성
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class HospitalAuthResponse {
    private String empNo;
    private Boolean auth;
    private LocalDateTime authRequestTime;
    private LocalDateTime confirmTime;
}
