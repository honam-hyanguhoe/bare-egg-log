package org.egglog.api.worktype.model.dto.request;

import jakarta.persistence.*;
import lombok.*;
import org.egglog.api.user.model.entity.User;
import org.egglog.api.worktype.model.entity.WorkTag;
import org.egglog.api.worktype.model.entity.WorkType;

import java.time.LocalTime;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.worktype.model.dto.request
 * fileName      : CreateWorkTypeRequest
 * description    : 사용자별 워크 타입 생성 요청 객체
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-30|김형민|최초 생성|
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateWorkTypeRequest {

    private String title;        //태그 이름
    private WorkTag workTag; //Day, Eve, Night, Off, ETC 태그 속성
    private String color;       //색상
    private String workTypeImgUrl;      //근무 이미지
    private LocalTime startTime;       //시작시간
    private LocalTime endTime;     //종료 시간

    public WorkType toEntity(User loginUser){
        return WorkType.builder()
                .title(this.title)
                .color(this.color)
                .workTag(this.workTag)
                .workTypeImgUrl(this.workTypeImgUrl)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .user(loginUser)
                .build();
    }
}
