package org.egglog.api.work.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;

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
public class EditAndDeleteWorkListRequest {
    @NotNull(message = "calendarGroupId 는 필수 입니다.")
    @Positive(message = "정확한 ID 값을 입력 해주세요.")
    private Long calendarGroupId;
    private List<EditAndDeleteWorkRequest> editWorkList;
}
