package org.egglog.api.hospital.model.dto.request;

import jakarta.persistence.Column;
import lombok.*;

/**
 * packageName    : org.egglog.api.hospital.model.dto.request
 * fileName       : CreateHospitalAuthReqeust
 * author         : 김형민
 * date           : 2024-04-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-26        김형민       최초 생성
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateHospitalAuthRequest {
    private String nurseCertificationImgUrl;
    private String hospitalCertificationImgUrl;
}
