package org.egglog.api.user.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * packageName    : org.egglog.api.user.model.dto.request
 * fileName       : UpdateFcmRequest
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
public class UpdateFcmRequest {
    private String fcmToken;
}
