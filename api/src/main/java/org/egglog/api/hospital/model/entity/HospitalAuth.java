package org.egglog.api.hospital.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.hospital.model.dto.response.HospitalAuthResponse;
import org.egglog.api.user.model.entity.User;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * packageName    : org.egglog.api.hospital.model.entity
 * fileName       : HospitalAuth
 * author         : 김형민
 * date           : 2024-04-26
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-04-26        김형민       최초 생성
 */
@Entity
@Getter
@Setter
@Table(name = "hospital_auth")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EntityListeners(AuditingEntityListener.class)
public class HospitalAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_auth_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    @Column
    private Boolean auth;

    @Column(name = "auth_request_time")
    private LocalDateTime authRequestTime;

    @Column(name = "confirm_time")
    private LocalDateTime confirmTime;

    @Column(name = "nurse_certification_img_url")
    private String nurseCertificationImgUrl;

    @Column(name = "hospital_certification_img_url")
    private String hospitalCertificationImgUrl;
    public HospitalAuth create(User user, String nurseCertificationImgUrl, String hospitalCertificationImgUrl){
        this.auth = false;
        this.authRequestTime = LocalDateTime.now();
        this.nurseCertificationImgUrl = nurseCertificationImgUrl;
        this.hospitalCertificationImgUrl = hospitalCertificationImgUrl;
        this.user = user;
        this.hospital = user.getSelectedHospital();
        return this;
    }

    public HospitalAuthResponse toResponse(){
        return HospitalAuthResponse.builder()
                .auth(this.auth)
                .authRequestTime(this.authRequestTime)
                .confirmTime(this.confirmTime!=null ? this.confirmTime : null)
                .nurseCertificationImgUrl(this.nurseCertificationImgUrl)
                .hospitalCertificationImgUrl(this.hospitalCertificationImgUrl)
                .build();
    }
}
