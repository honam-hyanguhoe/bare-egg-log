package org.egglog.api.security.model.dto.request;

import lombok.*;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.user.model.entity.enums.AuthProvider;
import org.egglog.api.user.model.entity.enums.UserRole;
import org.egglog.api.user.model.entity.enums.UserStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginRequest {
    private String name;
    private String email;
    private String profileImgUrl;

    public User toEntity(AuthProvider authProvider){
        return User.builder()
                .email(this.email)
                .name(this.name)
                .profileImgUrl(this.profileImgUrl)
                .provider(authProvider)
                .userStatus(UserStatus.ACTIVE)
                .userRole(UserRole.GENERAL_USER)
                .build();
    }
}
