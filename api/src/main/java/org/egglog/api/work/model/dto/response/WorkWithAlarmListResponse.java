package org.egglog.api.work.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.egglog.api.calendargroup.model.dto.response.CalendarGroupResponse;

import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.work.model.dto.response
 * fileName      : WorkWithListResponse
 * description    :
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-05-16|김형민|최초 생성|
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
public class WorkWithAlarmListResponse {
    private List<WorkWithAlarmResponse> workList;
    private CalendarGroupResponse calendarGroup;
}
