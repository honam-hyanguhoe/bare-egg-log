package org.egglog.api.user.model.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "유저 이름은 필수 입니다.")
    @Size(max = 60, message = "유저 이름이 너무 깁니다.")
    private String userName;
    @NotBlank(message = "프로필 이미지는 필수 입니다.")
    private String profileImgUrl;
    @NotNull(message = "변경할 병원 ID는 필수 입니다.")
    @Positive(message = "병원 아이디 값을 입력 해 주세요.")
    private Long selectHospitalId;
    @NotBlank(message = "변경할 사번은 필수입니다.")
    private String empNo;
}
