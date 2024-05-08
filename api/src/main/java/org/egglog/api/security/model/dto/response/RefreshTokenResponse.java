package org.egglog.api.security.model.dto.response;

import lombok.*;
import org.egglog.api.user.model.dto.response.UserResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponse {
    private TokenResponse tokens;
    private UserResponse userInfo;
}
