package org.egglog.api.worktype.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.user.model.entity.User;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class WorkType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_type_id")
    private Long id;

    @Column(name = "work_type_title")
    private String title;        //Day, Eve, Night, 교육, Off, 보건, None 등

    private String color;       //색상

    private String workTypeImgUrl;      //근무 이미지

    @Column(name = "work_start_time")
    private LocalTime startTime;       //시작시간

    @Column(name = "work_end_time")
    private LocalTime endTime;     //종료 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
