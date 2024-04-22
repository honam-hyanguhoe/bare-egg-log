package org.egglog.api.hospital.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.user.model.entity.Users;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "sido_code")
    private String sidoCode;

    @Column(name = "sido")
    private String sido;

    @Column(name = "gungu_code")
    private String gunguCode;

    @Column(name = "gungu")
    private String gungu;

    @Column(name = "dong")
    private String dong;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "address")
    private String address;

    @Column(name = "hospital_name")
    private String hospitalName;

    @Column(name = "lat")
    private String lat;  //위도

    @Column(name = "lng")
    private String lng;  //경도

    @OneToMany(mappedBy = "hospital")
    private List<Users> users = new ArrayList<>();

}
