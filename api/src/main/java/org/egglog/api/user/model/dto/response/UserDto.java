package org.egglog.api.user.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.nursetest.app.user.model.dto.request.FindUserRequest;
import com.nursetest.app.user.model.entity.User;
import com.nursetest.app.user.model.entity.enums.AuthProvider;
import com.nursetest.app.user.model.entity.enums.UserRole;
import com.nursetest.app.user.model.entity.enums.UserStatus;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.*;



@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_NULL)
@JsonNaming(SnakeCaseStrategy.class)
public class UserDto implements UserDetails {
    private Long id;
    private String email;
    private String password;
    private String userName;
    private AuthProvider provider;
    private String hospitalName;
    private String empNo;
    private String profileImgUrl;
    private UserRole role;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserDto of(User user){
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .provider(user.getProvider())
                .password(user.getPassword())
                .userName(user.getUserName())
                .hospitalName(user.getHospitalName())
                .empNo(user.getEmpNo())
                .profileImgUrl(user.getProfileImgUrl())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // todo : 사용자에게 부여된 권한을 반환합니다. 이는 GrantedAuthority 객체의 컬렉션으로 표현됩니다.
        // 예: 사용자가 "ADMIN"과 "USER" 권한을 가지고 있다면, 이 메서드는 이 두 권한을 나타내는 GrantedAuthority 객체를 포함하는 리스트를 반환해야 합니다
        return Collections.singleton(() -> role.name());
    }

    @Override
    public String getPassword() {
        // todo : 사용자의 비밀번호를 반환합니다. 인증 과정에서 사용됩니다.
        return password;
    }

    @Override
    public String getUsername() {
        //todo : 사용자의 이름 또는 ID를 반환합니다. 인증 과정에서 사용자를 식별하는 데 사용됩니다.
        return email;
    }
    public String getRealUserName(){
        return userName;
    }
    @Override
    public boolean isAccountNonExpired() {
        //todo : 계정이 만료되었는지 여부를 나타냅니다. 계정이 만료되면 false를 반환합니다.
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //todo : 계정이 잠겨 있는지 여부를 나타냅니다. 계정이 잠겨 있으면 false를 반환합니다.

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //todo : 사용자의 자격 증명(비밀번호)이 만료되었는지 여부를 나타냅니다. 만료되면 false를 반환합니다
        return true;
    }

    @Override
    public boolean isEnabled() {
        //todo : 사용자 계정이 활성화되어 있는지 여부를 나타냅니다. 비활성화되어 있으면 false를 반환합니다.
        if (status==UserStatus.INACTIVE){
            return false;
        }
        return true;
    }
}
