package org.egglog.api.user.model.entity;


import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.hospital.model.entity.Hospital;
import org.egglog.api.hospital.model.entity.HospitalAuth;
import org.egglog.api.user.model.dto.response.UserResponse;
import org.egglog.api.user.model.entity.enums.AuthProvider;
import org.egglog.api.user.model.entity.enums.UserRole;
import org.egglog.api.user.model.entity.enums.UserStatus;
import org.egglog.api.work.model.entity.Work;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.*;


/**
 * @Entity
 * @Table(name = "users")
 */
@Entity
@Getter
@Setter
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true, length = 254, name = "user_email")
    @EqualsAndHashCode.Include
    private String email;

    @Column(length = 255, name = "user_password")
    private String password;

    @Column(nullable = false, length = 50, name = "user_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "select_hospital_id")
    private Hospital selectedHospital;


    @Column(length = 2000, name = "profile_img_url")
    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    @Column( name = "role")
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus userStatus;

    @Column(length = 30, name = "emp_no")
    private String empNo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "login_at")
    private LocalDateTime loginAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "device_token")
    private String deviceToken;

    @Column(name = "work_group_id")
    private Long workGroupId;

    public User doLogin(){
        this.loginAt = LocalDateTime.now();
        return this;
    }
    public User doLogout(){
        this.deviceToken = null;
        return this;
    }

    public User delete(){
        this.name = "탈퇴회원";
        this.email = UUID.randomUUID().toString();
        this.empNo = null;
        this.userStatus = UserStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
        return this;
    }
    public User join(String joinUserName, Hospital joinHospital, String empNo, String token, Long workGroupId){
        this.name = joinUserName;
        this.selectedHospital = joinHospital;
        this.loginAt = LocalDateTime.now();
        this.deviceToken = token;
        this.empNo = empNo;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.workGroupId = workGroupId;
        return this;
    }

    public boolean isJoin() {
        return this.selectedHospital != null && this.empNo != null && this.name != null;
    }

    public User updateFcmToken(String token){
        this.deviceToken = token;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public User updateInfo(String updateUserName, String updateProfileImgUrl, Hospital selectHospital, String updateEmpNo){
        this.name = updateUserName;
        this.profileImgUrl = updateProfileImgUrl;
        this.selectedHospital = selectHospital;
        this.empNo = updateEmpNo;
        this.updatedAt = LocalDateTime.now();
        return this;
    }


    public UserResponse toResponse(){
        return UserResponse.builder()
                .id(this.id)
                .email(this.email)
                .userName(this.name)
                .selectedHospital(this.selectedHospital!=null ? this.selectedHospital.toUserHospitalResponse() : null)
                .hospitalAuth(null)
                .userRole(this.userRole)
                .empNo(this.empNo)
                .profileImgUrl(this.profileImgUrl)
                .userStatus(this.userStatus)
                .updatedAt(this.updatedAt)
                .createdAt(this.createdAt)
                .deviceToken(this.deviceToken)
                .loginAt(this.loginAt)
                .workGroupId(this.workGroupId)
                .build();
    }
    public UserResponse toResponse(HospitalAuth hospitalAuth){
        return UserResponse.builder()
                .id(this.id)
                .email(this.email)
                .userName(this.name)
                .selectedHospital(this.selectedHospital!=null ? this.selectedHospital.toUserHospitalResponse() : null)
                .hospitalAuth(hospitalAuth!=null ? hospitalAuth.toResponse() : null)
                .userRole(this.userRole)
                .empNo(this.empNo)
                .profileImgUrl(this.profileImgUrl)
                .userStatus(this.userStatus)
                .updatedAt(this.updatedAt)
                .createdAt(this.createdAt)
                .deviceToken(this.deviceToken)
                .loginAt(this.loginAt)
                .workGroupId(this.workGroupId)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> userRole.name());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        if (userStatus == UserStatus.INACTIVE) {
            return false;
        }
        return true;
    }

    public static User of(OAuth2User oAuth2User) {
        Map<String, Object> map = oAuth2User.getAttributes();
        User user = new User();
        String email = (String) map.get("email");
        String name = (String) map.get("name");
        String picture = (String) map.get("picture");
        AuthProvider provider = (AuthProvider) map.get("provider");

        if (email != null) user.setEmail(email);
        if (name != null) user.setName(name);
        if (picture != null) user.setProfileImgUrl(picture);
        if (provider != null) user.setProvider(provider);
        user.setUserStatus(UserStatus.ACTIVE);
        user.setUserRole(UserRole.GENERAL_USER);
        return user;
    }
}

