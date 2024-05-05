package org.egglog.api.work.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.work.model.dto.request
 * fileName      : EditAndDeleteWorkRequest
 * description    : 근무일정 리스트 수정 및 삭제 요청 객체
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-29|김형민|최초 생성|
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EditAndDeleteWorkRequest {
    @NotNull(message = "workId 는 필수 입니다.")
    @Positive(message = "정확한 ID 값을 입력 해주세요.")
    private Long workId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate workDate;

    @NotNull(message = "workTypeId 는 필수 입니다.")
    @Positive(message = "정확한 ID 값을 입력 해주세요.")
    private Long workTypeId;
    private Boolean isDeleted;
}
