package org.egglog.api.work.model.dto.response;

import lombok.*;
import org.egglog.api.calendargroup.model.dto.response.CalendarGroupResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * ```
 * ===================[Info]=========================
 * packageName    : org.egglog.api.work.model.dto.response
 * fileName      : WorkListResponse
 * description    : 근무 일정 리스트 응답 객체
 * =================================================
 * ```
 * |DATE|AUTHOR|NOTE|
 * |:---:|:---:|:---:|
 * |2024-04-30|김형민|최초 생성|
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
public class WorkListResponse {

    private List<WorkResponse> workList;
    private CalendarGroupResponse calendarGroup;

    public WorkListResponse() {
        this.workList = new ArrayList<>();
    }

    public void addWork(WorkResponse work) {
        this.workList.add(work);
    }
}
