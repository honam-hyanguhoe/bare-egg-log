package org.egglog.api.work.model.dto.request;

import lombok.*;

import java.time.LocalDate;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.work.model.dto.request
 * fileName      : FindMonthlyGroupUserWorkListRequest
 * description    : 같은 그룹에 속해있는 유저의 근무일정을 조회 요청 객체
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
public class FindGroupUserWorkListRequest {
    private Long calendarGroupId;
    private Long groupUserId;
    private LocalDate startDate;
    private LocalDate endDate;
}
