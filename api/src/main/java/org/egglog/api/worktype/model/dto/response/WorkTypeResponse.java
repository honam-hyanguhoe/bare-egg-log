package org.egglog.api.worktype.model.dto.response;

import lombok.*;
import java.time.LocalTime;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.worktype.model.dto.response
 * fileName      : WorkTypeResponse
 * description    : 근무 타입 응답 객체
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-29|김형민|최초 생성|
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkTypeResponse {
    private Long workTypeId;
    private String title;        //Day, Eve, Night, 교육, Off, 보건, None 등
    private String color;       //색상
    private String workTypeImgUrl;      //근무 이미지
    private LocalTime startTime;       //시작시간
    private LocalTime endTime;     //종료 시간
}
