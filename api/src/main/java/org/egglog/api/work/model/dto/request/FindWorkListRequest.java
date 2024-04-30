package org.egglog.api.work.model.dto.request;

import lombok.*;

import java.time.LocalDate;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.work.model.dto.request
 * fileName      : FindWorkListRequest
 * description    : 해당 캘린더 그룹의 근무 일정 리스트 응답 객체
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
public class FindWorkListRequest {
    private Long calendarGroupId;
    private LocalDate startDate;
    private LocalDate endDate;
}