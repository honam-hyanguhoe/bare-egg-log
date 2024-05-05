package org.egglog.api.hospital.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * packageName    : org.egglog.api.hospital.model.dto.request
 * fileName       : FindHospitalListRequest
 * author         : 김형민
 * date           : 2024-05-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-05-06        김형민       최초 생성
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FindHospitalListRequest {
    private String hospitalName; // 필수가 아님

    @NotNull(message = "시작 인덱스는 필수입니다.")
    private Integer offset; // 페이지네이션 시작 인덱스, 필수

    @NotNull(message = "페이지당 항목 수는 필수입니다.")
    private Integer limit;  // 페이지당 항목 수, 필수
}
