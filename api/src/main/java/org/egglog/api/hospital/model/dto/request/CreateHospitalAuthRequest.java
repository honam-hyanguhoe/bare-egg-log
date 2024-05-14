package org.egglog.api.hospital.model.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "간호사 인증 파일은 필수입니다.")
    private String nurseCertificationImgUrl;
    @NotBlank(message = "병원 재직 인증 파일은 필수입니다.")
    private String hospitalCertificationImgUrl;
}
