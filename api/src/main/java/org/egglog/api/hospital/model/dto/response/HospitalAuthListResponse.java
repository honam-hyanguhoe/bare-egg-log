package org.egglog.api.hospital.model.dto.response;

import lombok.*;
import org.egglog.api.user.model.entity.enums.UserStatus;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.hospital.model.dto.response
 * fileName      : HospitalAuthListResponse
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-02|김형민|최초 생성|
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HospitalAuthListResponse {
    private Long userId;
    private Long hospitalAuthId;
    private String userName;
    private String userEmail;
    private UserStatus userStatus;
    private UserHospitalResponse selectHospital;
    private HospitalAuthResponse hospitalAuth;
}
