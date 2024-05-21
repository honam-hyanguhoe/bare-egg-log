package org.egglog.api.worktype.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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

    @NotBlank(message = "태그 이름은 필수 입니다.")
    private String title;        //태그 이름 필수 너무길지 않게

    @NotBlank(message = "컬러코드는 필수 입니다.")
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "컬러 코드는 #000000 형식 입니다.")
    private String color;       //색상 필수 #00000의 형식인지 검증

    @NotBlank(message = "근무 이미지는 필수 입니다.")
    @Pattern(regexp = "^(http[s]?://.*|)$", message = "적절한 url을 입력해 주십시오.")
    private String workTypeImgUrl;      //근무 이미지 필수, url인지 검증

    @NotNull(message = "시작 시각은 필수입니다.")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;       //시작시간

    @NotNull(message = "근무 시간은 필수입니다.")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime workTime;     //시간
}
