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
 * fileName       : JoinUserRequest
 * author         : 김형민
 * date           : 2024-04-24
 * description    : 추가 정보 입력을 받아 로그인을 완료하는 정보 객체입니다.
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-24        김형민       최초 생성
 * 2024-05-06        김형민       검증 추가
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JoinUserRequest {
    @NotBlank(message = "유저 이름은 필수 입니다.")
    @Size(max = 60, message = "유저 이름이 너무 깁니다.")
    private String userName;

    @NotNull(message = "병원 선택은 필수 입니다.")
    @Positive(message = "병원 아이디 값을 입력 해 주세요.")
    private Long hospitalId;

    @NotBlank(message = "사번은 필수 입니다.")
    @Size(max = 30, message = "사번이 너무 깁니다.")
    private String empNo;

    @NotBlank(message = "FCM 토큰은 필수 입니다.")
    private String fcmToken;
}
