package org.egglog.api.hospital.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.user.model.entity.Users;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_id")
    private Long id;

    private String sidoCode;

    private String sido;

    private String gunguCode;

    private String gungu;

    private String dong;

    private String zipCode;

    private String address;

    private String hospitalName;

    private String lat;  //위도

    private String lng;  //경도

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

}
