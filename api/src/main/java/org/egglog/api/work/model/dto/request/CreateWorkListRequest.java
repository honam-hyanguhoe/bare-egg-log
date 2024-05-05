package org.egglog.api.work.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;


/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.work.model.dto.request
 * fileName      : CreateWorkListRequest
 * description    : 근무일정 리스트 추가 요청 객체
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
public class CreateWorkListRequest {
    @NotNull(message = "캘린더 ID는 필수 입니다.")
    @Positive(message = "정확한 ID값을 입력 해주세요.")
    private Long calendarGroupId;
    private List<CreateWorkRequest> workTypes;
}
