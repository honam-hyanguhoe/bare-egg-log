package org.egglog.api.user.model.entity;


import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.hospital.model.entity.Hospital;
import org.egglog.api.user.model.dto.response.UserResponse;
import org.egglog.api.user.model.entity.enums.AuthProvider;
import org.egglog.api.user.model.entity.enums.UserRole;
import org.egglog.api.user.model.entity.enums.UserStatus;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


/**
 * @Entity
 * @Table(name = "users")
 */
@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 254, name = "user_email")
    private String email;

    @Column(length = 255, name = "user_password")
    private String password;

    @Column(nullable = false, length = 50, name = "user_name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column(nullable = false, length = 30, name = "emp_no")
    private String empNo;

    @Column(length = 2000, name = "profile_img_url")
    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "role")
    private UserRole userRole;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private UserStatus userStatus;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "login_at")
    private LocalDateTime loginAt;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "is_hospital_auth")
    private Boolean isHospitalAuth;

    public User doLogin(){
        this.loginAt = LocalDateTime.now();
        return this;
    }

    public User deleteUser(){
        this.name = "탈퇴회원";
        this.empNo = null;
//        this.profileImgUrl = null;
        this.hospital = null;
        this.isHospitalAuth = false;
        this.userStatus = UserStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
        return this;
    }

    public User updateUserInfo(String updateUserName, String updateProfileImgUrl){
        this.name = updateUserName;
        this.profileImgUrl = updateProfileImgUrl;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public User updateHospitalInfo(Hospital updateHospital, String updateEmpNo){
        this.hospital = updateHospital;
        this.empNo = updateEmpNo;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public UserResponse toResponse(){
        return UserResponse.builder()
                .id(this.id)
                .email(this.email)
                .userName(this.name)
                .hospital(this.hospital.toUserHospitalResponse())
                .userRole(this.userRole)
                .profileImgUrl(this.profileImgUrl)
                .empNo(this.empNo)
                .userStatus(this.userStatus)
                .isHospitalAuth(this.isHospitalAuth)
                .updatedAt(this.updatedAt)
                .createdAt(this.createdAt)
                .loginAt(this.loginAt)
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
//        String nickName = (String) map.get("nickName");
//        String birthDateMonth = (String) map.get("birthDateMonth");
//        String birthDateYear = (String) map.get("birthDateYear");
//        String gender = (String) map.get("gender");
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

