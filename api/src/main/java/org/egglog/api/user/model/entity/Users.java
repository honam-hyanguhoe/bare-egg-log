package org.egglog.api.user.model.entity;


import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.hospital.model.entity.Hospital;
import org.egglog.api.user.model.dto.request.UpdateUserHospitalRequest;
import org.egglog.api.user.model.dto.request.UpdateUserRequest;
import org.egglog.api.user.model.dto.response.UserResponse;
import org.egglog.api.user.model.entity.enums.AuthProvider;
import org.egglog.api.user.model.entity.enums.UserRole;
import org.egglog.api.user.model.entity.enums.UserStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;


/**
 * @Entity
 * @Table(name = "users")
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 254, name = "user_email")
    private String email;

    @Column(length = 255, name = "user_password")
    private String password;

    @Column(nullable = false, length = 50, name = "user_name")
    private String userName;

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

    public Users doLogin(){
        this.loginAt = LocalDateTime.now();
        return this;
    }

    public Users deleteUser(){
        this.userName = "탈퇴회원";
        this.empNo = null;
//        this.profileImgUrl = null;
        this.hospital = null;
        this.isHospitalAuth = false;
        this.userStatus = UserStatus.DELETED;
        this.deletedAt = LocalDateTime.now();
        return this;
    }

    public Users updateUserInfo(String updateUserName, String updateProfileImgUrl){
        this.userName = updateUserName;
        this.profileImgUrl = updateProfileImgUrl;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public Users updateHospitalInfo(Hospital updateHospital, String updateEmpNo){
        this.hospital = updateHospital;
        this.empNo = updateEmpNo;
        this.updatedAt = LocalDateTime.now();
        return this;
    }

    public UserResponse toResponse(){
        return UserResponse.builder()
                .id(this.id)
                .email(this.email)
                .userName(this.userName)
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
}

