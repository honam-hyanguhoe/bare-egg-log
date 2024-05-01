package org.egglog.api.worktype.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.worktype.model.dto.response.WorkTypeResponse;

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
    private String title;        //태그 이름

    @Enumerated(EnumType.STRING)
    private WorkTag workTag; //Day, Eve, Night, Off, ETC 태그 속성

    @Column(name = "work_type_color")
    private String color;       //색상

    @Column(name = "work_type_img_url")
    private String workTypeImgUrl;      //근무 이미지

    @Column(name = "work_start_time")
    private LocalTime startTime;       //시작시간

    @Column(name = "work_time")
    private LocalTime workTime;     //근무 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public WorkTypeResponse toResponse(){
        return WorkTypeResponse.builder()
                .workTypeId(this.id)
                .title(this.title)
                .workTag(this.workTag)
                .color(this.color)
                .workTypeImgUrl(this.workTypeImgUrl)
                .startTime(this.startTime)
                .workTime(this.workTime)
                .build();
    }

    public WorkType edit(String title, String color, String workTypeImgUrl, LocalTime startTime, LocalTime workTime){
        this.title = title;
        this.workTag = WorkTag.ETC;
        this.color = color;
        this.workTypeImgUrl = workTypeImgUrl;
        this.startTime = startTime;
        this.workTime = workTime;
        return this;
    }
}
