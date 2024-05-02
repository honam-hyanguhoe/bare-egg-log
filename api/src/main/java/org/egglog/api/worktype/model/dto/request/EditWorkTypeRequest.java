package org.egglog.api.worktype.model.dto.request;

import lombok.*;
import org.egglog.api.worktype.model.entity.WorkTag;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.worktype.model.dto.request
 * fileName      : EditWorkTypeRequest
 * description    : 사용자별 워크 타입 수정 요청 객체
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
public class EditWorkTypeRequest {
    private Long workTypeId;
    private String title;        //태그 이름
    private String color;       //색상
    private String workTypeImgUrl;      //근무 이미지
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;       //시작 시각
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime workTime;     //근무 시간
}
