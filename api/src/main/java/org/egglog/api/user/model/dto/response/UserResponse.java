package org.egglog.api.user.model.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.egglog.api.hospital.model.dto.response.UserHospitalResponse;
import org.egglog.api.user.model.entity.enums.UserRole;
import org.egglog.api.user.model.entity.enums.UserStatus;


/**
 * @packageName    : org.egglog.api.user.model.dto.response
 * @fileName       : UserResponse
 * @author         : 김형민
 * @date           : 2024-04-22
 * @description    : 공개할 수 있는 유저 정보 객체입니다.
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-22        김형민       최초 생성
 */
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserResponse {
    private Long id;
    private String email;
    private String userName;
    private UserHospitalResponse hospital;
    private String empNo;
    private String profileImgUrl;
    private UserRole userRole;
    private UserStatus userStatus;
    private Boolean isHospitalAuth;
}
