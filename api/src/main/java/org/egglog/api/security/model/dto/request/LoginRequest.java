package org.egglog.api.security.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "유저 이름은 필수 입니다.")
    @Size(max = 60, message = "유저 이름이 너무 깁니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입니다.")
    @Email(message = "이메일 형식을 지켜주십시오.")
    private String email;

    @NotBlank(message = "프로필 이미지는 필수 입니다.")
    @Pattern(regexp = "^(http[s]?://.*|)$", message = "적절한 url을 입력해 주십시오.")
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
