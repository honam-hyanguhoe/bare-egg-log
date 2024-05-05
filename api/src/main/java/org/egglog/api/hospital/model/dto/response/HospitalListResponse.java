package org.egglog.api.hospital.model.dto.response;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalListResponse {

    private int offset;
    private int limit;
    private List<UserHospitalResponse> hospitals;
}
