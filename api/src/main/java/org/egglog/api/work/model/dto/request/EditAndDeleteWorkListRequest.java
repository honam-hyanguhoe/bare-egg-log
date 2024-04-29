package org.egglog.api.work.model.dto.request;

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
    private Long calendarGroupId;
    private List<EditAndDeleteWorkRequest> editWorkList;
}
